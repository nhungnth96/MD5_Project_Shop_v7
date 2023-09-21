package md5.end.repository;

import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);
//    Page<Product> findByProductNameContaining(String name, Pageable pageable);
//    Page<Product> findAll(Pageable pageable);
//    Page<Product> findProductByStatus(boolean status,Pageable pageable);
}
