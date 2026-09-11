package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeViewResponse {
    private Long id;
    private Instant startTime;
    private Instant endTime;
    private Long roomId;
    private String roomName;
    private Long cinemaId;
    private String cinemaName;
    private String cinemaAddress;
}
