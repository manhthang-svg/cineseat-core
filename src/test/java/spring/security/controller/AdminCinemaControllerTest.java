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
import spring.security.dto.request.CreateCinemaRequest;
import spring.security.dto.request.UpdateCinemaRequest;
import spring.security.dto.response.CinemaResponse;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.CinemaService;

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
class AdminCinemaControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private AdminCinemaController adminCinemaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminCinemaController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void createCinema_Success_Returns201() throws Exception {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("CineVault Central")
                .provinceCode("79")
                .districtCode("765")
                .wardCode("26830")
                .detailAddress("123 Main Street")
                .build();

        CinemaResponse response = CinemaResponse.builder()
                .id(1L)
                .name("CineVault Central")
                .provinceCode("79")
                .provinceName("Thành phố Hồ Chí Minh")
                .districtCode("765")
                .districtName("Quận Bình Thạnh")
                .wardCode("26830")
                .wardName("Phường 22")
                .detailAddress("123 Main Street")
                .address("123 Main Street, Phường 22, Quận Bình Thạnh, Thành phố Hồ Chí Minh")
                .build();

        when(cinemaService.createCinema(any(CreateCinemaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/cinemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("CineVault Central"))
                .andExpect(jsonPath("$.data.provinceCode").value("79"))
                .andExpect(jsonPath("$.data.districtCode").value("765"))
                .andExpect(jsonPath("$.data.wardCode").value("26830"));

        verify(cinemaService).createCinema(any(CreateCinemaRequest.class));
    }

    @Test
    void createCinema_InvalidRequest_Returns400() throws Exception {
        CreateCinemaRequest request = CreateCinemaRequest.builder()
                .name("")
                .provinceCode("")
                .districtCode("")
                .wardCode("")
                .detailAddress("")
                .build();

        mockMvc.perform(post("/api/admin/cinemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void updateCinema_Success_Returns200() throws Exception {
        Long cinemaId = 1L;
        UpdateCinemaRequest request = UpdateCinemaRequest.builder()
                .name("CineVault Central Updated")
                .provinceCode("79")
                .districtCode("765")
                .wardCode("26830")
                .detailAddress("123 Main Street")
                .build();

        CinemaResponse response = CinemaResponse.builder()
                .id(cinemaId)
                .name("CineVault Central Updated")
                .build();

        when(cinemaService.updateCinema(eq(cinemaId), any(UpdateCinemaRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/admin/cinemas/{id}", cinemaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("CineVault Central Updated"));
    }

    @Test
    void deleteCinema_Success_Returns204() throws Exception {
        Long cinemaId = 1L;
        doNothing().when(cinemaService).deleteCinema(cinemaId);

        mockMvc.perform(delete("/api/admin/cinemas/{id}", cinemaId))
                .andExpect(status().isNoContent());

        verify(cinemaService).deleteCinema(cinemaId);
    }

    @Test
    void deleteCinema_HasRooms_Returns409() throws Exception {
        Long cinemaId = 1L;
        doThrow(new AppException(ErrorCode.CINEMA_HAS_ROOMS))
                .when(cinemaService).deleteCinema(cinemaId);

        mockMvc.perform(delete("/api/admin/cinemas/{id}", cinemaId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.CINEMA_HAS_ROOMS.getCode()));
    }
}
