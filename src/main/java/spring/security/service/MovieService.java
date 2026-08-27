package spring.security.service;

import spring.security.dto.request.CreateMovieRequest;
import spring.security.dto.request.UpdateMovieRequest;
import spring.security.dto.response.MovieResponse;
import spring.security.dto.response.PageResponse;
import spring.security.enums.MovieSort;

public interface MovieService {
    PageResponse<MovieResponse> browse(String query, int page, int size, MovieSort sort);

    MovieResponse createMovie(CreateMovieRequest request);

    MovieResponse updateMovie(Long id, UpdateMovieRequest request);

    void deleteMovie(Long id);
}
