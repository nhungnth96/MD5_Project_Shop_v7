package md5.end.repository;

import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);

    @Query ("select p from Product p where LOWER(p.name) like %:name% and p.status = 1")
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query ("select p from Product p where p.status = 1")
    Page<Product> findAll(Pageable pageable);


    Page<Product> findByStatus(int status,Pageable pageable);



}
