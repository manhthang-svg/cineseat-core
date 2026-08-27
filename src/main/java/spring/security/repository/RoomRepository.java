package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.entity.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByIdAndDeletedFalse(Long id);

    List<Room> findAllByCinemaIdAndDeletedFalse(Long cinemaId);

    boolean existsByCinemaIdAndDeletedFalse(Long cinemaId);

    boolean existsByCinemaIdAndNameIgnoreCaseAndDeletedFalse(Long cinemaId, String name);

    boolean existsByCinemaIdAndNameIgnoreCaseAndDeletedFalseAndIdNot(Long cinemaId, String name, Long id);
}
