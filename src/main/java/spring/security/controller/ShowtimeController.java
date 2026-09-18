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
import spring.security.dto.response.ShowtimeSeatMapResponse;
import spring.security.service.ShowtimeService;

@Validated
@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtimes", description = "Public showtime and seat layout APIs")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping("/{id}/seats")
    @Operation(summary = "Get seat map and availability for a showtime")
    public ApiResponse<ShowtimeSeatMapResponse> getShowtimeSeats(@PathVariable Long id) {
        return ApiResponse.success(showtimeService.getShowtimeSeats(id));
    }
}
