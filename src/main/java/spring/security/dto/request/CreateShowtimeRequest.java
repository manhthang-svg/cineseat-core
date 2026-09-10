package spring.security.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class CreateShowtimeRequest {

    @NotNull(message = "Movie ID must not be null")
    private Long movieId;

    @NotNull(message = "Room ID must not be null")
    private Long roomId;

    @NotNull(message = "Start time must not be null")
    private Instant startTime;

    @NotNull(message = "End time must not be null")
    private Instant endTime;

}
