package spring.security.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spring.security.enums.SeatType;
import spring.security.enums.ShowtimeSeatStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeSeatResponse {
    private Long id;
    private Long seatId;
    private String rowLabel;
    private Integer seatNumber;
    private SeatType seatType;
    private BigDecimal price;
    private ShowtimeSeatStatus status;
}
