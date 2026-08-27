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
import spring.security.dto.response.CinemaResponse;
import spring.security.service.CinemaService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
@Tag(name = "Cinemas", description = "Public cinema discovery APIs")
public class CinemaController {

    private final CinemaService cinemaService;
    private final spring.security.service.RoomService roomService;

    @GetMapping
    @Operation(summary = "Browse active cinemas")
    public ApiResponse<List<CinemaResponse>> getAllCinemas() {
        return ApiResponse.success(cinemaService.getActiveCinemas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get cinema details by ID")
    public ApiResponse<CinemaResponse> getCinemaById(@PathVariable Long id) {
        return ApiResponse.success(cinemaService.getCinemaById(id));
    }

    @GetMapping("/{cinemaId}/rooms")
    @Operation(summary = "Get active rooms in cinema")
    public ApiResponse<List<spring.security.dto.response.RoomResponse>> getRoomsByCinemaId(@PathVariable Long cinemaId) {
        return ApiResponse.success(roomService.getRoomsByCinemaId(cinemaId));
    }
}
