package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.entity.BookingSeat;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    boolean existsBySeatIdAndDeletedFalse(Long seatId);
}
