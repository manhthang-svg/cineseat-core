-- Add a separate email column while preserving accounts created by the
-- original API, where username was the user's email address.
ALTER TABLE users
    ADD COLUMN email VARCHAR(100) NULL AFTER username;

UPDATE users
SET email = username
WHERE email IS NULL;

ALTER TABLE users
    MODIFY COLUMN email VARCHAR(100) NOT NULL,
    ADD CONSTRAINT uk_users_email UNIQUE (email);

CREATE TABLE movies (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NULL,
    duration_minutes INT NOT NULL,
    release_date DATE NOT NULL,
    poster_url VARCHAR(2048) NULL,
    status VARCHAR(20) NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_movies_title UNIQUE (title),
    CONSTRAINT fk_movies_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_movies_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_movies_created_by ON movies(created_by_id);
CREATE INDEX idx_movies_updated_by ON movies(updated_by_id);

CREATE TABLE cinemas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(500) NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_cinemas_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_cinemas_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_cinemas_created_by ON cinemas(created_by_id);
CREATE INDEX idx_cinemas_updated_by ON cinemas(updated_by_id);

CREATE TABLE rooms (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    cinema_id BIGINT NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_rooms_cinema_name UNIQUE (cinema_id, name),
    CONSTRAINT fk_rooms_cinema FOREIGN KEY (cinema_id) REFERENCES cinemas(id),
    CONSTRAINT fk_rooms_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_rooms_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_rooms_created_by ON rooms(created_by_id);
CREATE INDEX idx_rooms_updated_by ON rooms(updated_by_id);

CREATE TABLE seats (
    id BIGINT NOT NULL AUTO_INCREMENT,
    row_label VARCHAR(10) NOT NULL,
    seat_number INT NOT NULL,
    seat_type VARCHAR(20) NOT NULL,
    room_id BIGINT NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_seats_room_position UNIQUE (room_id, row_label, seat_number),
    CONSTRAINT fk_seats_room FOREIGN KEY (room_id) REFERENCES rooms(id),
    CONSTRAINT fk_seats_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_seats_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_seats_created_by ON seats(created_by_id);
CREATE INDEX idx_seats_updated_by ON seats(updated_by_id);

CREATE TABLE showtimes (
    id BIGINT NOT NULL AUTO_INCREMENT,
    start_time DATETIME(6) NOT NULL,
    end_time DATETIME(6) NOT NULL,
    movie_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_showtimes_movie FOREIGN KEY (movie_id) REFERENCES movies(id),
    CONSTRAINT fk_showtimes_room FOREIGN KEY (room_id) REFERENCES rooms(id),
    CONSTRAINT fk_showtimes_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_showtimes_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_showtimes_movie_start ON showtimes(movie_id, start_time);
CREATE INDEX idx_showtimes_room_start ON showtimes(room_id, start_time);
CREATE INDEX idx_showtimes_created_by ON showtimes(created_by_id);
CREATE INDEX idx_showtimes_updated_by ON showtimes(updated_by_id);

CREATE TABLE bookings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    expires_at DATETIME(6) NOT NULL,
    user_id BIGINT NOT NULL,
    showtime_id BIGINT NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_bookings_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_bookings_showtime FOREIGN KEY (showtime_id) REFERENCES showtimes(id),
    CONSTRAINT fk_bookings_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_bookings_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_showtime ON bookings(showtime_id);
CREATE INDEX idx_bookings_status_expires ON bookings(status, expires_at);
CREATE INDEX idx_bookings_created_by ON bookings(created_by_id);
CREATE INDEX idx_bookings_updated_by ON bookings(updated_by_id);

CREATE TABLE booking_seats (
    id BIGINT NOT NULL AUTO_INCREMENT,
    price DECIMAL(12, 2) NOT NULL,
    booking_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_booking_seats_booking_seat UNIQUE (booking_id, seat_id),
    CONSTRAINT fk_booking_seats_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
    CONSTRAINT fk_booking_seats_seat FOREIGN KEY (seat_id) REFERENCES seats(id),
    CONSTRAINT fk_booking_seats_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_booking_seats_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_booking_seats_seat ON booking_seats(seat_id);
CREATE INDEX idx_booking_seats_created_by ON booking_seats(created_by_id);
CREATE INDEX idx_booking_seats_updated_by ON booking_seats(updated_by_id);

CREATE TABLE payments (
    id BIGINT NOT NULL AUTO_INCREMENT,
    amount DECIMAL(12, 2) NOT NULL,
    payment_method VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(255) NULL,
    paid_at DATETIME(6) NULL,
    booking_id BIGINT NOT NULL,
    created_by_id BIGINT NULL,
    updated_by_id BIGINT NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_payments_transaction_id UNIQUE (transaction_id),
    CONSTRAINT fk_payments_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
    CONSTRAINT fk_payments_created_by FOREIGN KEY (created_by_id) REFERENCES users(id),
    CONSTRAINT fk_payments_updated_by FOREIGN KEY (updated_by_id) REFERENCES users(id)
);

CREATE INDEX idx_payments_booking ON payments(booking_id);
CREATE INDEX idx_payments_created_by ON payments(created_by_id);
CREATE INDEX idx_payments_updated_by ON payments(updated_by_id);
