package spring.security.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import spring.security.dto.request.CreateMovieRequest;
import spring.security.dto.request.UpdateMovieRequest;
import spring.security.dto.response.MovieResponse;
import spring.security.entity.Movie;
import spring.security.enums.ErrorCode;
import spring.security.enums.MovieSort;
import spring.security.enums.MovieStatus;
import spring.security.exceptions.AppException;
import spring.security.mapper.MovieMapper;
import spring.security.repository.MovieRepository;
import spring.security.repository.ShowtimeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;
    @Mock
    private MovieMapper movieMapper;

    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieServiceImpl(movieRepository, showtimeRepository, movieMapper);
    }

    @Test
    void browseReturnsOnlyActiveMoviesAndNormalizesQuery() {
        Movie movie = new Movie();
        movie.setTitle("Interstellar");
        MovieResponse response = MovieResponse.builder()
                .id(1L)
                .title("Interstellar")
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieRepository.findByStatusAndDeletedFalseAndTitleContainingIgnoreCase(
                eq(MovieStatus.ACTIVE), eq("Interstellar"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(movie)));
        when(movieMapper.toResponse(movie)).thenReturn(response);

        var result = movieService.browse("  Interstellar  ", 0, 12, MovieSort.LATEST);

        assertThat(result.content()).containsExactly(response);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    void browseUsesRequestedPagingAndSort() {
        when(movieRepository.findByStatusAndDeletedFalseAndTitleContainingIgnoreCase(
                eq(MovieStatus.ACTIVE), eq(""), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        movieService.browse(null, 2, 8, MovieSort.OLDEST);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(movieRepository).findByStatusAndDeletedFalseAndTitleContainingIgnoreCase(
                eq(MovieStatus.ACTIVE), eq(""), pageableCaptor.capture());

        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(8);
        assertThat(pageable.getSort().getOrderFor("releaseDate").isAscending()).isTrue();
    }

    @Test
    void createMovie_Success() {
        CreateMovieRequest request = CreateMovieRequest.builder()
                .title("  Dune: Part Two  ")
                .description("Epic sci-fi")
                .durationMinutes(166)
                .releaseDate(LocalDate.of(2024, 3, 1))
                .build();

        Movie movieEntity = new Movie();
        Movie savedMovie = new Movie();
        savedMovie.setId(1L);
        savedMovie.setTitle("Dune: Part Two");
        savedMovie.setStatus(MovieStatus.ACTIVE);

        MovieResponse response = MovieResponse.builder()
                .id(1L)
                .title("Dune: Part Two")
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieRepository.existsByTitleIgnoreCaseAndDeletedFalse("Dune: Part Two")).thenReturn(false);
        when(movieMapper.toEntity(request)).thenReturn(movieEntity);
        when(movieRepository.save(movieEntity)).thenReturn(savedMovie);
        when(movieMapper.toResponse(savedMovie)).thenReturn(response);

        MovieResponse result = movieService.createMovie(request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Dune: Part Two");
        assertThat(movieEntity.getTitle()).isEqualTo("Dune: Part Two");
        assertThat(movieEntity.getStatus()).isEqualTo(MovieStatus.ACTIVE);
        verify(movieRepository).save(movieEntity);
    }

    @Test
    void createMovie_DuplicateTitle_ThrowsException() {
        CreateMovieRequest request = CreateMovieRequest.builder()
                .title("  Dune  ")
                .durationMinutes(155)
                .releaseDate(LocalDate.of(2021, 10, 22))
                .build();

        when(movieRepository.existsByTitleIgnoreCaseAndDeletedFalse("Dune")).thenReturn(true);

        assertThatThrownBy(() -> movieService.createMovie(request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_ALREADY_EXISTS);

        verify(movieRepository, never()).save(any());
    }

    @Test
    void updateMovie_Success() {
        Long movieId = 1L;
        UpdateMovieRequest request = UpdateMovieRequest.builder()
                .title("  Dune Part One (Updated)  ")
                .description("Updated description")
                .durationMinutes(156)
                .releaseDate(LocalDate.of(2021, 10, 22))
                .status(MovieStatus.ACTIVE)
                .build();

        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);
        existingMovie.setTitle("Dune Part One");

        Movie savedMovie = new Movie();
        savedMovie.setId(movieId);
        savedMovie.setTitle("Dune Part One (Updated)");

        MovieResponse response = MovieResponse.builder()
                .id(movieId)
                .title("Dune Part One (Updated)")
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieRepository.findByIdAndDeletedFalse(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitleIgnoreCaseAndDeletedFalseAndIdNot("Dune Part One (Updated)", movieId)).thenReturn(false);
        when(movieRepository.save(existingMovie)).thenReturn(savedMovie);
        when(movieMapper.toResponse(savedMovie)).thenReturn(response);

        MovieResponse result = movieService.updateMovie(movieId, request);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Dune Part One (Updated)");
        verify(movieMapper).updateEntityFromDto(request, existingMovie);
        verify(movieRepository).save(existingMovie);
    }

    @Test
    void updateMovie_NotFound_ThrowsException() {
        Long movieId = 99L;
        UpdateMovieRequest request = UpdateMovieRequest.builder()
                .title("Non-existent")
                .durationMinutes(120)
                .releaseDate(LocalDate.now())
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieRepository.findByIdAndDeletedFalse(movieId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.updateMovie(movieId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_NOT_FOUND);

        verify(movieRepository, never()).save(any());
    }

    @Test
    void updateMovie_DuplicateTitle_ThrowsException() {
        Long movieId = 1L;
        UpdateMovieRequest request = UpdateMovieRequest.builder()
                .title("Another Movie")
                .durationMinutes(120)
                .releaseDate(LocalDate.now())
                .status(MovieStatus.ACTIVE)
                .build();

        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);

        when(movieRepository.findByIdAndDeletedFalse(movieId)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.existsByTitleIgnoreCaseAndDeletedFalseAndIdNot("Another Movie", movieId)).thenReturn(true);

        assertThatThrownBy(() -> movieService.updateMovie(movieId, request))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_ALREADY_EXISTS);

        verify(movieRepository, never()).save(any());
    }

    @Test
    void deleteMovie_Success() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);
        existingMovie.setStatus(MovieStatus.ACTIVE);
        existingMovie.setDeleted(false);

        when(movieRepository.findByIdAndDeletedFalse(movieId)).thenReturn(Optional.of(existingMovie));
        when(showtimeRepository.existsByMovieIdAndDeletedFalse(movieId)).thenReturn(false);

        movieService.deleteMovie(movieId);

        assertThat(existingMovie.getDeleted()).isTrue();
        assertThat(existingMovie.getStatus()).isEqualTo(MovieStatus.INACTIVE);
        verify(movieRepository).save(existingMovie);
    }

    @Test
    void deleteMovie_NotFound_ThrowsException() {
        Long movieId = 99L;
        when(movieRepository.findByIdAndDeletedFalse(movieId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.deleteMovie(movieId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_NOT_FOUND);

        verify(movieRepository, never()).save(any());
    }

    @Test
    void deleteMovie_HasShowtimes_ThrowsException() {
        Long movieId = 1L;
        Movie existingMovie = new Movie();
        existingMovie.setId(movieId);

        when(movieRepository.findByIdAndDeletedFalse(movieId)).thenReturn(Optional.of(existingMovie));
        when(showtimeRepository.existsByMovieIdAndDeletedFalse(movieId)).thenReturn(true);

        assertThatThrownBy(() -> movieService.deleteMovie(movieId))
                .isInstanceOf(AppException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MOVIE_HAS_SHOWTIMES);

        verify(movieRepository, never()).save(any());
    }
}
