package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.security.entity.Showtime;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    boolean existsByMovieIdAndDeletedFalse(Long movieId);

    boolean existsByRoomIdAndDeletedFalse(Long roomId);

    @Query("""
            SELECT s
            FROM Showtime s
            JOIN FETCH s.room r
            JOIN FETCH r.cinema c
            WHERE s.movie.id = :movieId
              AND s.deleted = false
              AND r.deleted = false
              AND c.deleted = false
              AND s.startTime >= :from
              AND s.startTime < :to
            ORDER BY s.startTime ASC, c.name ASC, r.name ASC
            """)
    List<Showtime> findAvailableByMovieAndStartTimeBetween(
            @Param("movieId") Long movieId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );

    @Query(
            """
            SELECT COUNT(s) > 0
            FROM Showtime s
            WHERE s.room.id = :roomId
              AND s.deleted = false
              AND (:showtimeId IS NULL OR s.id <> :showtimeId)
              AND s.startTime < :newEndTime
              AND s.endTime > :newStartTime
            """
    )
    boolean existsAnOverlappingShowtime(
            @Param("roomId") Long roomId,
            @Param("showtimeId") Long showtimeId,
            @Param("newStartTime") Instant newStartTime,
            @Param("newEndTime") Instant newEndTime
    );

}
