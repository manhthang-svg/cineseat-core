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
import spring.security.dto.request.CreateSeatRequest;
import spring.security.dto.request.UpdateSeatRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.SeatResponse;
import spring.security.service.SeatService;

import static spring.security.config.OpenApiConfig.BEARER_AUTH_SCHEME;

@Validated
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Seats", description = "Admin seat management APIs")
@SecurityRequirement(name = BEARER_AUTH_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class AdminSeatController {

    private final SeatService seatService;

    @PostMapping("/rooms/{roomId}/seats")
    @Operation(summary = "Create a seat in room")
    public ResponseEntity<ApiResponse<SeatResponse>> createSeat(
            @PathVariable Long roomId,
            @Valid @RequestBody CreateSeatRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(seatService.createSeat(roomId, request)));
    }

    @PutMapping("/seats/{id}")
    @Operation(summary = "Update an existing seat")
    public ApiResponse<SeatResponse> updateSeat(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSeatRequest request
    ) {
        return ApiResponse.success(seatService.updateSeat(id, request));
    }

    @DeleteMapping("/seats/{id}")
    @Operation(summary = "Delete (deactivate) a seat")
    public ResponseEntity<Void> deleteSeat(
            @PathVariable Long id
    ) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
