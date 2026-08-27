package spring.security.dto.response;

import lombok.Builder;
import lombok.Getter;
import spring.security.enums.MovieStatus;

import java.time.LocalDate;

@Getter
@Builder
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private LocalDate releaseDate;
    private String posterUrl;
    private MovieStatus status;
}
