package spring.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.request.CreateSeatRequest;
import spring.security.dto.request.UpdateSeatRequest;
import spring.security.dto.response.SeatResponse;
import spring.security.enums.ErrorCode;
import spring.security.enums.SeatType;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.SeatService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminSeatControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SeatService seatService;

    @InjectMocks
    private AdminSeatController adminSeatController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminSeatController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void createSeat_Success_Returns201() throws Exception {
        Long roomId = 10L;
        CreateSeatRequest request = CreateSeatRequest.builder()
                .rowLabel("A")
                .seatNumber(1)
                .seatType(SeatType.REGULAR)
                .build();

        SeatResponse response = SeatResponse.builder()
                .id(100L)
                .roomId(roomId)
                .rowLabel("A")
                .seatNumber(1)
                .seatCode("A1")
                .seatType(SeatType.REGULAR)
                .build();

        when(seatService.createSeat(eq(roomId), any(CreateSeatRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/rooms/{roomId}/seats", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(100))
                .andExpect(jsonPath("$.data.seatCode").value("A1"));

        verify(seatService).createSeat(eq(roomId), any(CreateSeatRequest.class));
    }

    @Test
    void createSeat_InvalidSeatNumber_Returns400() throws Exception {
        Long roomId = 10L;
        CreateSeatRequest request = CreateSeatRequest.builder()
                .rowLabel("A")
                .seatNumber(0)
                .seatType(SeatType.REGULAR)
                .build();

        mockMvc.perform(post("/api/admin/rooms/{roomId}/seats", roomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void updateSeat_Success_Returns200() throws Exception {
        Long seatId = 100L;
        UpdateSeatRequest request = UpdateSeatRequest.builder()
                .rowLabel("A")
                .seatNumber(2)
                .seatType(SeatType.VIP)
                .build();

        SeatResponse response = SeatResponse.builder()
                .id(seatId)
                .rowLabel("A")
                .seatNumber(2)
                .seatCode("A2")
                .seatType(SeatType.VIP)
                .build();

        when(seatService.updateSeat(eq(seatId), any(UpdateSeatRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/admin/seats/{id}", seatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.seatCode").value("A2"));
    }

    @Test
    void deleteSeat_Success_Returns204() throws Exception {
        Long seatId = 100L;
        doNothing().when(seatService).deleteSeat(seatId);

        mockMvc.perform(delete("/api/admin/seats/{id}", seatId))
                .andExpect(status().isNoContent());

        verify(seatService).deleteSeat(seatId);
    }

    @Test
    void deleteSeat_HasBookings_Returns409() throws Exception {
        Long seatId = 100L;
        doThrow(new AppException(ErrorCode.SEAT_HAS_BOOKINGS))
                .when(seatService).deleteSeat(seatId);

        mockMvc.perform(delete("/api/admin/seats/{id}", seatId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.SEAT_HAS_BOOKINGS.getCode()));
    }
}
