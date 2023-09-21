package md5.end.repository;

import md5.end.model.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDetailRepository extends JpaRepository<OrderDetail,Long> {
}
