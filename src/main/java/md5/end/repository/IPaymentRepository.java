package md5.end.repository;

import md5.end.model.entity.order.Payment;
import md5.end.model.entity.order.PaymentType;
import md5.end.model.entity.user.Role;
import md5.end.model.entity.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByType(PaymentType paymentType);
}
