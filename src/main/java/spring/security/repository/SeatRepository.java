package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.entity.Seat;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByIdAndDeletedFalse(Long id);

    List<Seat> findAllByRoomIdAndDeletedFalse(Long roomId);

    boolean existsByRoomIdAndDeletedFalse(Long roomId);

    boolean existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalse(Long roomId, String rowLabel, Integer seatNumber);

    boolean existsByRoomIdAndRowLabelIgnoreCaseAndSeatNumberAndDeletedFalseAndIdNot(Long roomId, String rowLabel, Integer seatNumber, Long id);
}
