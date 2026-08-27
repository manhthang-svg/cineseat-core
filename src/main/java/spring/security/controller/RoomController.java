package spring.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.dto.response.ApiResponse;
import spring.security.dto.response.SeatResponse;
import spring.security.service.SeatService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Public room and seat discovery APIs")
public class RoomController {

    private final SeatService seatService;

    @GetMapping("/{roomId}/seats")
    @Operation(summary = "Get active seats in room")
    public ApiResponse<List<SeatResponse>> getSeatsByRoomId(@PathVariable Long roomId) {
        return ApiResponse.success(seatService.getSeatsByRoomId(roomId));
    }
}
