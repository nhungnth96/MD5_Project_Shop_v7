package md5.end.service.impl;

import md5.end.exception.NotFoundException;
import md5.end.model.entity.order.Payment;
import md5.end.model.entity.order.PaymentType;
import md5.end.model.entity.order.Shipping;
import md5.end.model.entity.user.Role;
import md5.end.repository.IPaymentRepository;
import md5.end.repository.IRoleRepository;
import md5.end.service.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;
    @Override
    public Payment findByType(PaymentType paymentType) throws NotFoundException {
        Optional<Payment> paymentOptional = paymentRepository.findByType(paymentType);
        if(!paymentOptional.isPresent()){
            throw new NotFoundException(paymentType+" not found.");
        }
        return paymentOptional.get();
    }
    public Payment findById(Long id) throws NotFoundException {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if(!paymentOptional.isPresent()){
            throw new NotFoundException("Payment' s id "+id+" not found.");
        }
        return paymentOptional.get();
    }
}
