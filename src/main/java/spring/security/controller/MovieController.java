package spring.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.MovieResponse;
import spring.security.dto.response.PageResponse;
import spring.security.dto.response.ShowtimeViewResponse;
import spring.security.enums.MovieSort;
import spring.security.service.MovieService;
import spring.security.service.ShowtimeService;

import java.time.Instant;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Public movie discovery APIs")
public class MovieController {
    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    @GetMapping
    @Operation(summary = "Browse active movies")
    public ApiResponse<PageResponse<MovieResponse>> browse(
            @RequestParam(defaultValue = "") @Size(max = 100) String query,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "12") @Min(1) @Max(24) int size,
            @RequestParam(defaultValue = "LATEST") MovieSort sort
    ) {
        return ApiResponse.success(movieService.browse(query, page, size, sort));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get movie details by ID")
    public ApiResponse<MovieResponse> getMovieById(@PathVariable Long id) {
        return ApiResponse.success(movieService.getMovieById(id));
    }

    @GetMapping("/{id}/showtimes")
    @Operation(summary = "Get available showtimes for a movie in a time range")
    public ApiResponse<List<ShowtimeViewResponse>> getMovieShowtimes(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return ApiResponse.success(showtimeService.getMovieShowtimes(id, from, to));
    }
}
