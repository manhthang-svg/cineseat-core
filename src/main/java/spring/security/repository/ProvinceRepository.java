package spring.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.security.entity.Province;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    List<Province> findAllByOrderByNameAsc();
}
