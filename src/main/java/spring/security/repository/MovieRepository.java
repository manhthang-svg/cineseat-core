package spring.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.entity.Movie;
import spring.security.enums.MovieStatus;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByStatusAndDeletedFalseAndTitleContainingIgnoreCase(
            MovieStatus status,
            String title,
            Pageable pageable
    );

    Optional<Movie> findByIdAndDeletedFalse(Long id);

    boolean existsByTitleIgnoreCaseAndDeletedFalse(String title);

    boolean existsByTitleIgnoreCaseAndDeletedFalseAndIdNot(String title, Long id);
}
