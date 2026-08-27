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
import spring.security.dto.request.CreateRoomRequest;
import spring.security.dto.request.UpdateRoomRequest;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.RoomResponse;
import spring.security.service.RoomService;

import static spring.security.config.OpenApiConfig.BEARER_AUTH_SCHEME;

@Validated
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Rooms", description = "Admin room management APIs")
@SecurityRequirement(name = BEARER_AUTH_SCHEME)
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoomController {

    private final RoomService roomService;

    @PostMapping("/cinemas/{cinemaId}/rooms")
    @Operation(summary = "Create a room in cinema")
    public ResponseEntity<ApiResponse<RoomResponse>> createRoom(
            @PathVariable Long cinemaId,
            @Valid @RequestBody CreateRoomRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(roomService.createRoom(cinemaId, request)));
    }

    @PutMapping("/rooms/{id}")
    @Operation(summary = "Update an existing room")
    public ApiResponse<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomRequest request
    ) {
        return ApiResponse.success(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/rooms/{id}")
    @Operation(summary = "Delete (deactivate) a room")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id
    ) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
