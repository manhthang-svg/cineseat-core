package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.security.entity.Showtime;

import java.time.Instant;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    boolean existsByMovieIdAndDeletedFalse(Long movieId);

    boolean existsByRoomIdAndDeletedFalse(Long roomId);
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
