package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.CartItemRequest;

import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.response.CartItemResponse;
import md5.end.model.dto.response.OrderResponse;
import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.order.Order;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.user.User;
import md5.end.repository.IBrandRepository;
import md5.end.repository.ICartItemRepository;
import md5.end.repository.IOrderRepository;
import md5.end.repository.IProductRepository;
import md5.end.security.principal.UserDetailService;
import md5.end.service.ICartItemService;
import md5.end.service.amapper.BrandMapper;
import md5.end.service.amapper.CartItemMapper;
import md5.end.service.amapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private ICartItemRepository cartItemRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IOrderRepository orderRepository;
    @Override
    public List<CartItemResponse> findAll() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        return cartItems.stream()
                .map(cartItem -> cartItemMapper.getResponseFromEntity(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public List<CartItemResponse> findAllByUserId(Long id) {
        List<CartItem> cartItems = cartItemRepository.findAllByUserId(id);
        return cartItems.stream()
                .map(cartItem -> cartItemMapper.getResponseFromEntity(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponse findById(Long id) throws NotFoundException {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if(!cartItemOptional.isPresent()){
            throw new NotFoundException("Cart item's id "+id+" not found.");
        }
        return cartItemMapper.getResponseFromEntity(cartItemOptional.get());
    }



    @Override
    public CartItemResponse findByProductId(Long id) throws NotFoundException {

        Optional<CartItem> cartItemOptional = cartItemRepository.findByProductId(id);
        if(!cartItemOptional.isPresent()){
            throw new NotFoundException("Product's id "+id+" not found.");
        }
        return cartItemMapper.getResponseFromEntity(cartItemOptional.get());
    }

    @Override
    public CartItemResponse save(CartItemRequest cartItemRequest) throws NotFoundException {
        User currentUser = userDetailService.getCurrentUser();
        Long productId = cartItemRequest.getProductId();

        Optional<CartItem> existingCartItemOptional = cartItemRepository.findByUserIdAndProductId(currentUser.getId(), productId);

        if (existingCartItemOptional.isPresent()) {
            CartItem existingCartItem = existingCartItemOptional.get();
            int newQuantity = existingCartItem.getQuantity() + cartItemRequest.getQuantity();
            existingCartItem.setQuantity(newQuantity);
            return cartItemMapper.getResponseFromEntity(cartItemRepository.save(existingCartItem));
        } else {
            Optional<Product> productOptional = productRepository.findById(productId);
            if (!productOptional.isPresent()) {
                throw new NotFoundException("Product's id " + productId + " not found.");
            } else {
                CartItem cartItem = cartItemMapper.getEntityFromRequest(cartItemRequest);
                return cartItemMapper.getResponseFromEntity(cartItemRepository.save(cartItem));
            }
        }
}

    @Override
    public CartItemResponse update(CartItemRequest cartItemRequest, Long id) throws NotFoundException {
        User currentUser = userDetailService.getCurrentUser();
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if (!cartItemOptional.isPresent()) {
            throw new NotFoundException("Cart item's id "+id+" not found.");
        }

        if(cartItemOptional.get().getUser().getId().equals(currentUser.getId())) {
            cartItemRequest.setProductId(cartItemOptional.get().getProduct().getId());
            CartItem cartItem = cartItemMapper.getEntityFromRequest(cartItemRequest);
            cartItem.setId(id);
            return cartItemMapper.getResponseFromEntity(cartItemRepository.save(cartItem));
        } else {
            throw new NotFoundException("Cart item's id "+id+" is not yours.");
        }

    }

    @Override
    public CartItemResponse deleteById(Long id) throws NotFoundException {
        User currentUser = userDetailService.getCurrentUser();
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if(!cartItemOptional.isPresent()){
            throw new NotFoundException("Cart item's id "+id+" not found.");
        }
        if(cartItemOptional.get().getUser().getId().equals(currentUser.getId())) {
            cartItemRepository.deleteById(id);
            return cartItemMapper.getResponseFromEntity(cartItemOptional.get());
        } else {
            throw new NotFoundException("Cart item's id "+id+" is not yours.");
        }

    }


    public OrderResponse checkout(OrderRequest orderRequest) throws NotFoundException, BadRequestException {
        List<CartItem> cartItems = cartItemRepository.findAllByUserId(userDetailService.getCurrentUser().getId());
        Order order = orderMapper.getEntityFromRequest(orderRequest);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
        return orderMapper.getResponseFromEntity(order);
    }

}
