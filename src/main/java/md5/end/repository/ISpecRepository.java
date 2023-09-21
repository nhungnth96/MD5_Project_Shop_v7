package md5.end.repository;

import md5.end.model.entity.product.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISpecRepository extends JpaRepository<Specification,Long> {
        Optional<Specification> findByName(String name);
}

