package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.security.enums.SeatType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    private Long id;
    private Long roomId;
    private String rowLabel;
    private Integer seatNumber;
    private String seatCode;
    private SeatType seatType;
    private Instant createdAt;
    private Instant updatedAt;
}
