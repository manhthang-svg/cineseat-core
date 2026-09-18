package spring.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import spring.security.enums.ShowtimeSeatStatus;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(
    name = "showtime_seats",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_showtime_seats_showtime_seat",
            columnNames = {"showtime_id", "seat_id"}
        )
    }
)
public class ShowtimeSeat extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShowtimeSeatStatus status = ShowtimeSeatStatus.AVAILABLE;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
