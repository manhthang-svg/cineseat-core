package spring.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.request.CreateMovieRequest;
import spring.security.dto.request.UpdateMovieRequest;
import spring.security.dto.response.MovieResponse;
import spring.security.enums.ErrorCode;
import spring.security.enums.MovieStatus;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.MovieService;

import java.time.LocalDate;

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
class AdminMovieControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private MovieService movieService;

    @InjectMocks
    private AdminMovieController adminMovieController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminMovieController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void createMovie_Success_Returns201() throws Exception {
        CreateMovieRequest request = CreateMovieRequest.builder()
                .title("Inception")
                .description("A mind-bending thriller")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .status(MovieStatus.ACTIVE)
                .build();

        MovieResponse response = MovieResponse.builder()
                .id(1L)
                .title("Inception")
                .description("A mind-bending thriller")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieService.createMovie(any(CreateMovieRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/admin/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Inception"));

        verify(movieService).createMovie(any(CreateMovieRequest.class));
    }

    @Test
    void createMovie_InvalidDuration_Returns400() throws Exception {
        CreateMovieRequest request = CreateMovieRequest.builder()
                .title("Too Short")
                .durationMinutes(10) // Below 30 mins
                .releaseDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/api/admin/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void updateMovie_Success_Returns200() throws Exception {
        Long movieId = 1L;
        UpdateMovieRequest request = UpdateMovieRequest.builder()
                .title("Inception Re-release")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .status(MovieStatus.ACTIVE)
                .build();

        MovieResponse response = MovieResponse.builder()
                .id(movieId)
                .title("Inception Re-release")
                .durationMinutes(148)
                .releaseDate(LocalDate.of(2010, 7, 16))
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieService.updateMovie(eq(movieId), any(UpdateMovieRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/admin/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Inception Re-release"));

        verify(movieService).updateMovie(eq(movieId), any(UpdateMovieRequest.class));
    }

    @Test
    void updateMovie_NotFound_Returns404() throws Exception {
        Long movieId = 99L;
        UpdateMovieRequest request = UpdateMovieRequest.builder()
                .title("Non-existent")
                .durationMinutes(120)
                .releaseDate(LocalDate.now())
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieService.updateMovie(eq(movieId), any(UpdateMovieRequest.class)))
                .thenThrow(new AppException(ErrorCode.MOVIE_NOT_FOUND));

        mockMvc.perform(put("/api/admin/movies/{id}", movieId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.MOVIE_NOT_FOUND.getCode()));
    }

    @Test
    void deleteMovie_Success_Returns204() throws Exception {
        Long movieId = 1L;
        doNothing().when(movieService).deleteMovie(movieId);

        mockMvc.perform(delete("/api/admin/movies/{id}", movieId))
                .andExpect(status().isNoContent());

        verify(movieService).deleteMovie(movieId);
    }

    @Test
    void deleteMovie_HasShowtimes_Returns409() throws Exception {
        Long movieId = 1L;
        doThrow(new AppException(ErrorCode.MOVIE_HAS_SHOWTIMES))
                .when(movieService).deleteMovie(movieId);

        mockMvc.perform(delete("/api/admin/movies/{id}", movieId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(ErrorCode.MOVIE_HAS_SHOWTIMES.getCode()));
    }
}
