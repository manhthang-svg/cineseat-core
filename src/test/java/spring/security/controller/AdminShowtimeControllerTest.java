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
import spring.security.dto.request.CreateShowtimeRequest;
import spring.security.dto.response.CreateShowtimeResponse;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.ShowtimeService;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminShowtimeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Mock
    private ShowtimeService showtimeService;

    @InjectMocks
    private AdminShowtimeController adminShowtimeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminShowtimeController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void createShowtime_Success_Returns201() throws Exception {
        CreateShowtimeRequest request = validRequest();
        CreateShowtimeResponse response = CreateShowtimeResponse.builder()
                .id(10L)
                .movieId(1L)
                .roomId(2L)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        when(showtimeService.createShowtime(any(CreateShowtimeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(10))
                .andExpect(jsonPath("$.data.movieId").value(1))
                .andExpect(jsonPath("$.data.roomId").value(2));
    }

    @Test
    void createShowtime_MissingFields_Returns400() throws Exception {
        mockMvc.perform(post("/api/admin/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void createShowtime_Overlap_Returns409() throws Exception {
        when(showtimeService.createShowtime(any(CreateShowtimeRequest.class)))
                .thenThrow(new AppException(ErrorCode.OVERLAP_SHOWTIME_EXCEPTION));

        mockMvc.perform(post("/api/admin/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SHOWTIME_001"));
    }

    private CreateShowtimeRequest validRequest() {
        CreateShowtimeRequest request = new CreateShowtimeRequest();
        request.setMovieId(1L);
        request.setRoomId(2L);
        request.setStartTime(Instant.parse("2026-09-20T03:00:00Z"));
        request.setEndTime(Instant.parse("2026-09-20T05:00:00Z"));
        return request;
    }
}
