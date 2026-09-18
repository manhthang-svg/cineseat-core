package spring.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.response.ShowtimeSeatMapResponse;
import spring.security.dto.response.ShowtimeSeatResponse;
import spring.security.enums.ErrorCode;
import spring.security.enums.SeatType;
import spring.security.enums.ShowtimeSeatStatus;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.ShowtimeService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShowtimeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ShowtimeService showtimeService;

    @InjectMocks
    private ShowtimeController showtimeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(showtimeController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void getShowtimeSeats_Success_Returns200AndSeatMap() throws Exception {
        ShowtimeSeatResponse seat1 = ShowtimeSeatResponse.builder()
                .id(101L)
                .seatId(12L)
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.REGULAR)
                .price(BigDecimal.valueOf(90000))
                .status(ShowtimeSeatStatus.AVAILABLE)
                .build();

        ShowtimeSeatResponse seat2 = ShowtimeSeatResponse.builder()
                .id(102L)
                .seatId(13L)
                .rowLabel("A")
                .seatNumber(2)
                .seatType(SeatType.VIP)
                .price(BigDecimal.valueOf(120000))
                .status(ShowtimeSeatStatus.SOLD)
                .build();

        ShowtimeSeatMapResponse mapResponse = ShowtimeSeatMapResponse.builder()
                .showtimeId(1L)
                .movieTitle("Khoảng Trời Rực Rỡ")
                .cinemaName("CGV Landmark 81")
                .roomName("Screen 01")
                .startTime(Instant.parse("2026-09-20T19:00:00Z"))
                .endTime(Instant.parse("2026-09-20T21:15:00Z"))
                .totalSeats(2)
                .availableSeats(1)
                .seats(List.of(seat1, seat2))
                .build();

        when(showtimeService.getShowtimeSeats(1L)).thenReturn(mapResponse);

        mockMvc.perform(get("/api/showtimes/1/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.showtimeId").value(1))
                .andExpect(jsonPath("$.data.movieTitle").value("Khoảng Trời Rực Rỡ"))
                .andExpect(jsonPath("$.data.cinemaName").value("CGV Landmark 81"))
                .andExpect(jsonPath("$.data.roomName").value("Screen 01"))
                .andExpect(jsonPath("$.data.totalSeats").value(2))
                .andExpect(jsonPath("$.data.availableSeats").value(1))
                .andExpect(jsonPath("$.data.seats[0].id").value(101))
                .andExpect(jsonPath("$.data.seats[0].seatId").value(12))
                .andExpect(jsonPath("$.data.seats[0].rowLabel").value("A"))
                .andExpect(jsonPath("$.data.seats[0].seatNumber").value(1))
                .andExpect(jsonPath("$.data.seats[0].seatType").value("REGULAR"))
                .andExpect(jsonPath("$.data.seats[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$.data.seats[1].status").value("SOLD"));
    }

    @Test
    void getShowtimeSeats_NotFound_Returns404() throws Exception {
        when(showtimeService.getShowtimeSeats(99L))
                .thenThrow(new AppException(ErrorCode.SHOWTIME_NOT_FOUND));

        mockMvc.perform(get("/api/showtimes/99/seats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SHOWTIME_004"));
    }
}
