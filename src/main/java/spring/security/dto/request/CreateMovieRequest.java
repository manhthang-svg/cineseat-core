package spring.security.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.security.enums.MovieStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovieRequest {

    @NotBlank(message = "Movie title must not be blank")
    @Size(max = 255, message = "Movie title must not exceed 255 characters")
    private String title;

    private String description;

    @NotNull(message = "Duration is required")
    @Min(value = 30, message = "Duration must be at least 30 minutes")
    @Max(value = 300, message = "Duration must not exceed 300 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Release date is required")
    private LocalDate releaseDate;

    @Size(max = 2048, message = "Poster URL must not exceed 2048 characters")
    private String posterUrl;

    private MovieStatus status;
}
