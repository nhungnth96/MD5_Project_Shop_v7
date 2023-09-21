package md5.end.repository;

import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository <CartItem,Long>{
    List<CartItem> findAllByUserId(Long id);
    Optional<CartItem> findByProductId(Long id);
    Optional<CartItem> findByUserId(Long id);
    Optional<CartItem> findByUserIdAndProductId(Long userId,Long productId);

}
