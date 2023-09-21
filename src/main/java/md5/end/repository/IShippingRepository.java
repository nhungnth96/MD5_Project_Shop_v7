package md5.end.repository;

import md5.end.model.entity.order.Payment;
import md5.end.model.entity.order.PaymentType;
import md5.end.model.entity.order.Shipping;
import md5.end.model.entity.order.ShippingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IShippingRepository extends JpaRepository<Shipping,Long> {
        Optional<Shipping> findByType(ShippingType shippingType);
}
