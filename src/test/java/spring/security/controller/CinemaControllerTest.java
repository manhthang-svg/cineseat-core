package spring.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.response.CinemaResponse;
import spring.security.enums.ErrorCode;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.CinemaService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CinemaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CinemaService cinemaService;

    @InjectMocks
    private CinemaController cinemaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cinemaController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void getAllCinemas_Returns200() throws Exception {
        CinemaResponse response = CinemaResponse.builder().id(1L).name("CineVault Landmark").build();
        when(cinemaService.getActiveCinemas()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/cinemas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("CineVault Landmark"));
    }

    @Test
    void getCinemaById_Returns200() throws Exception {
        Long id = 1L;
        CinemaResponse response = CinemaResponse.builder().id(id).name("CineVault Landmark").build();
        when(cinemaService.getCinemaById(id)).thenReturn(response);

        mockMvc.perform(get("/api/cinemas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getCinemaById_NotFound_Returns404() throws Exception {
        Long id = 99L;
        when(cinemaService.getCinemaById(id)).thenThrow(new AppException(ErrorCode.CINEMA_NOT_FOUND));

        mockMvc.perform(get("/api/cinemas/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.CINEMA_NOT_FOUND.getCode()));
    }
}
