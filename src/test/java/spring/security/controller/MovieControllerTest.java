package spring.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.security.dto.response.MovieResponse;
import spring.security.dto.response.PageResponse;
import spring.security.enums.ErrorCode;
import spring.security.enums.MovieSort;
import spring.security.enums.MovieStatus;
import spring.security.exceptions.AppException;
import spring.security.exceptions.GlobalException;
import spring.security.service.MovieService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
                .setControllerAdvice(new GlobalException())
                .build();
    }

    @Test
    void browseMovies_Returns200() throws Exception {
        MovieResponse movie = MovieResponse.builder()
                .id(1L)
                .title("Interstellar")
                .durationMinutes(169)
                .releaseDate(LocalDate.of(2014, 11, 7))
                .status(MovieStatus.ACTIVE)
                .build();

        PageResponse<MovieResponse> pageResponse = new PageResponse<>(
                List.of(movie),
                0,
                12,
                1,
                1,
                true,
                true
        );

        when(movieService.browse(anyString(), anyInt(), anyInt(), eq(MovieSort.LATEST)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Interstellar"));
    }

    @Test
    void getMovieById_Success_Returns200() throws Exception {
        Long movieId = 1L;
        MovieResponse response = MovieResponse.builder()
                .id(movieId)
                .title("Dune: Part Two")
                .description("Epic sci-fi movie")
                .durationMinutes(166)
                .releaseDate(LocalDate.of(2024, 3, 1))
                .status(MovieStatus.ACTIVE)
                .build();

        when(movieService.getMovieById(movieId)).thenReturn(response);

        mockMvc.perform(get("/api/movies/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Dune: Part Two"))
                .andExpect(jsonPath("$.data.durationMinutes").value(166));
    }

    @Test
    void getMovieById_NotFound_Returns404() throws Exception {
        Long movieId = 99L;
        when(movieService.getMovieById(movieId)).thenThrow(new AppException(ErrorCode.MOVIE_NOT_FOUND));

        mockMvc.perform(get("/api/movies/{id}", movieId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.MOVIE_NOT_FOUND.getCode()));
    }
}
