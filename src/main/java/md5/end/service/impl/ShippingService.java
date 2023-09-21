package md5.end.service.impl;

import md5.end.exception.NotFoundException;
import md5.end.model.entity.order.Payment;
import md5.end.model.entity.order.Shipping;
import md5.end.model.entity.order.ShippingType;
import md5.end.repository.IRoleRepository;
import md5.end.repository.IShippingRepository;
import md5.end.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingService implements IShippingService {
    @Autowired
    private IShippingRepository shippingRepository;
    @Override
    public Shipping findByType(ShippingType shippingType) throws NotFoundException {
        Optional<Shipping> shippingOptional = shippingRepository.findByType(shippingType);
        if(!shippingOptional.isPresent()){
            throw new NotFoundException(shippingType+" not found.");
        }
        return shippingOptional.get();
    }

    public Shipping findById(Long id) throws NotFoundException {
        Optional<Shipping> shippingOptional = shippingRepository.findById(id);
        if(!shippingOptional.isPresent()){
            throw new NotFoundException("Shipping' s id "+id+" not found.");
        }
        return shippingOptional.get();
    }
}
