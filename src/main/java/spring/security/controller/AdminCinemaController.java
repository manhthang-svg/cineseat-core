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
import spring.security.dto.request.CreateCinemaRequest;
import spring.security.dto.request.UpdateCinemaRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.CinemaResponse;
import spring.security.service.CinemaService;

import static spring.security.config.OpenApiConfig.BEARER_AUTH_SCHEME;

@Validated
@RestController
@RequestMapping("/api/admin/cinemas")
@RequiredArgsConstructor
@Tag(name = "Admin Cinemas", description = "Admin cinema management APIs")
@SecurityRequirement(name = BEARER_AUTH_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class AdminCinemaController {

    private final CinemaService cinemaService;

    @PostMapping
    @Operation(summary = "Create a new cinema")
    public ResponseEntity<ApiResponse<CinemaResponse>> createCinema(
            @Valid @RequestBody CreateCinemaRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(cinemaService.createCinema(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing cinema")
    public ApiResponse<CinemaResponse> updateCinema(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCinemaRequest request
    ) {
        return ApiResponse.success(cinemaService.updateCinema(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete (deactivate) a cinema")
    public ResponseEntity<Void> deleteCinema(
            @PathVariable Long id
    ) {
        cinemaService.deleteCinema(id);
        return ResponseEntity.noContent().build();
    }
}
