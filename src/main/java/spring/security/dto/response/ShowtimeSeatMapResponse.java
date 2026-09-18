package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeSeatMapResponse {
    private Long showtimeId;
    private String movieTitle;
    private String cinemaName;
    private String roomName;
    private Instant startTime;
    private Instant endTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private List<ShowtimeSeatResponse> seats;
}
