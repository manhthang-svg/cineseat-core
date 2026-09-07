package spring.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.security.dto.request.CreateMovieRequest;
import spring.security.dto.request.UpdateMovieRequest;
import spring.security.dto.response.MovieResponse;
import spring.security.dto.response.PageResponse;
import spring.security.entity.Movie;
import spring.security.enums.ErrorCode;
import spring.security.enums.MovieSort;
import spring.security.enums.MovieStatus;
import spring.security.exceptions.AppException;
import spring.security.mapper.MovieMapper;
import spring.security.repository.MovieRepository;
import spring.security.repository.ShowtimeRepository;
import spring.security.service.MovieService;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;
    private final MovieMapper movieMapper;

    @Override
    @Transactional
    public MovieResponse createMovie(CreateMovieRequest request) {
        String normalizedTitle = request.getTitle().trim();
        if (movieRepository.existsByTitleIgnoreCaseAndDeletedFalse(normalizedTitle)) {
            throw new AppException(ErrorCode.MOVIE_ALREADY_EXISTS);
        }

        Movie movie = movieMapper.toEntity(request);
        movie.setTitle(normalizedTitle);
        if (movie.getStatus() == null) {
            movie.setStatus(MovieStatus.ACTIVE);
        }

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(savedMovie);
    }

    @Override
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "movie_details", key = "#id"),
                    @CacheEvict(value = "movie_list", allEntries = true)
            }
    )
    public MovieResponse updateMovie(Long id, UpdateMovieRequest request) {
        Movie movie = movieRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        String normalizedTitle = request.getTitle().trim();
        if (movieRepository.existsByTitleIgnoreCaseAndDeletedFalseAndIdNot(normalizedTitle, id)) {
            throw new AppException(ErrorCode.MOVIE_ALREADY_EXISTS);
        }

        movieMapper.updateEntityFromDto(request, movie);
        movie.setTitle(normalizedTitle);

        Movie updatedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(updatedMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));

        if (showtimeRepository.existsByMovieIdAndDeletedFalse(id)) {
            throw new AppException(ErrorCode.MOVIE_HAS_SHOWTIMES);
        }

        movie.setDeleted(true);
        movie.setStatus(MovieStatus.INACTIVE);
        movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "movie_list",key = "{#query,#page,#size,#sort}")
    public PageResponse<MovieResponse> browse(String query, int page, int size, MovieSort sort) {
        String normalizedQuery = query == null ? "" : query.trim();
        Pageable pageable = PageRequest.of(page, size, sortFor(sort));
        Page<MovieResponse> movies = movieRepository
                .findByStatusAndDeletedFalseAndTitleContainingIgnoreCase(
                        MovieStatus.ACTIVE,
                        normalizedQuery,
                        pageable
                )
                .map(movieMapper::toResponse);

        return PageResponse.from(movies);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "movie_details", key = "#id")
    public MovieResponse getMovieById(Long id) {
        return movieRepository.findByIdAndDeletedFalse(id)
                .map(movieMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    private Sort sortFor(MovieSort sort) {
        return switch (sort) {
            case OLDEST -> Sort.by(Sort.Order.asc("releaseDate"), Sort.Order.asc("id"));
            case TITLE_ASC -> Sort.by(Sort.Order.asc("title"), Sort.Order.asc("id"));
            case TITLE_DESC -> Sort.by(Sort.Order.desc("title"), Sort.Order.desc("id"));
            case LATEST -> Sort.by(Sort.Order.desc("releaseDate"), Sort.Order.desc("id"));
        };
    }
}
