package md5.end.repository;

import md5.end.model.entity.product.Brand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IBrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findByName(String name);
}
