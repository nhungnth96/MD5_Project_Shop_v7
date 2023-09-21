package md5.end.repository;

import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);
}
