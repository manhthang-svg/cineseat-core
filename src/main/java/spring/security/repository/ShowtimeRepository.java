package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.entity.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    boolean existsByMovieIdAndDeletedFalse(Long movieId);

    boolean existsByRoomIdAndDeletedFalse(Long roomId);
}
