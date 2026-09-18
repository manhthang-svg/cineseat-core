CREATE TABLE showtime_seats (
    id BIGINT NOT NULL AUTO_INCREMENT,

    showtime_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    price DECIMAL(12, 2) NOT NULL,

    booking_id BIGINT NULL,

    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
        ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,

    PRIMARY KEY (id),

    CONSTRAINT uk_showtime_seats_showtime_seat
        UNIQUE (showtime_id, seat_id),

    CONSTRAINT fk_showtime_seats_showtime
        FOREIGN KEY (showtime_id) REFERENCES showtimes(id),

    CONSTRAINT fk_showtime_seats_seat
        FOREIGN KEY (seat_id) REFERENCES seats(id),

    CONSTRAINT fk_showtime_seats_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(id),

    CONSTRAINT fk_showtime_seats_created_by
        FOREIGN KEY (created_by_id) REFERENCES users(id),

    CONSTRAINT fk_showtime_seats_updated_by
        FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_showtime_seats_showtime_status
    ON showtime_seats(showtime_id, status);

INSERT INTO showtime_seats (
    showtime_id,
    seat_id,
    status,
    price
)
SELECT
    st.id,
    s.id,
    'AVAILABLE',
    CASE
        WHEN s.seat_type = 'VIP' THEN 120000
        WHEN s.seat_type = 'COUPLE' THEN 180000
        ELSE 90000
    END
FROM showtimes st
JOIN seats s ON s.room_id = st.room_id
WHERE st.deleted = FALSE
  AND s.deleted = FALSE;
