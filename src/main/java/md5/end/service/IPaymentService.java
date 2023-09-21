package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.entity.order.Payment;
import md5.end.model.entity.order.PaymentType;

import java.util.Optional;

public interface IPaymentService {
    Payment findByType(PaymentType paymentType) throws NotFoundException;
}
