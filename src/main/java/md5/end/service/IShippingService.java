package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.entity.order.Shipping;
import md5.end.model.entity.order.ShippingType;

import java.util.Optional;

public interface IShippingService {
    Shipping findByType(ShippingType shippingType) throws NotFoundException;
}
