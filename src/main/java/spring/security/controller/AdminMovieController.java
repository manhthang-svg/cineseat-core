package spring.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.dto.request.CreateMovieRequest;
import spring.security.dto.request.UpdateMovieRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.MovieResponse;
import spring.security.service.MovieService;

import static spring.security.config.OpenApiConfig.BEARER_AUTH_SCHEME;

@Validated
@RestController
@RequestMapping("/api/admin/movies")
@RequiredArgsConstructor
@Tag(name = "Admin Movies", description = "Admin movie management APIs")
@SecurityRequirement(name = BEARER_AUTH_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class AdminMovieController {

    private final MovieService movieService;

    @PostMapping
    @Operation(summary = "Create a new movie")
    public ResponseEntity<ApiResponse<MovieResponse>> createMovie(
            @Valid @RequestBody CreateMovieRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(movieService.createMovie(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing movie")
    public ApiResponse<MovieResponse> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMovieRequest request
    ) {
        return ApiResponse.success(movieService.updateMovie(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (deactivate) a movie")
    public ResponseEntity<Void> deleteMovie(
            @PathVariable Long id
    ) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
