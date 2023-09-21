package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.CartItemRequest;
import md5.end.model.dto.response.CartItemResponse;
import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.user.User;

import java.util.List;


public interface ICartItemService extends IGenericService<CartItemRequest, CartItemResponse> {
    List<CartItemResponse> findAllByUserId(Long id);
    CartItemResponse findByProductId(Long id) throws NotFoundException;
}
