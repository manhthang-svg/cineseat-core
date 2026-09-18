package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.security.entity.ShowtimeSeat;

import java.util.List;

@Repository
public interface ShowtimeSeatRepository extends JpaRepository<ShowtimeSeat, Long> {

    @Query("""
            SELECT ss
            FROM ShowtimeSeat ss
            JOIN FETCH ss.seat s
            WHERE ss.showtime.id = :showtimeId
              AND ss.deleted = false
              AND s.deleted = false
            ORDER BY s.rowLabel ASC, s.seatNumber ASC
            """)
    List<ShowtimeSeat> findByShowtimeIdWithSeatOrderByPosition(@Param("showtimeId") Long showtimeId);
}
