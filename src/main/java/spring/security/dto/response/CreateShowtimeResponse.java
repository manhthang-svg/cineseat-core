package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShowtimeResponse {

    private Long id;

    private Long movieId;
    private Long roomId;

    private Instant startTime;
    private Instant endTime;


}
