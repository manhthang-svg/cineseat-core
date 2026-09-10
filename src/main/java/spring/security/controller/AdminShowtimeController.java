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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.service.ShowtimeService;

import static spring.security.config.OpenApiConfig.BEARER_AUTH_SCHEME;

@Validated
@RestController
@RequestMapping("/api/admin/showtimes")
@RequiredArgsConstructor
@Tag(name = "Admin Showtimes", description = "Admin showtime management APIs")
@SecurityRequirement(name = BEARER_AUTH_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class AdminShowtimeController {

    private final ShowtimeService showtimeService;

    @PostMapping
    @Operation(summary = "Create a new showtime")
    public ResponseEntity<ApiResponse<CreateShowtimeResponse>> createShowtime(
            @Valid @RequestBody CreateShowtimeRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(showtimeService.createShowtime(request)));
    }
}
