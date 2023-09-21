package md5.end.service.amapper;

import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.CartItemRequest;
import md5.end.model.dto.response.BrandResponse;
import md5.end.model.dto.response.CartItemResponse;
import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.user.User;
import md5.end.repository.IProductRepository;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CartItemMapper implements IGenericMapper<CartItem, CartItemRequest, CartItemResponse> {
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    IProductRepository productRepository;
    @Override
    public CartItem getEntityFromRequest(CartItemRequest cartItemRequest) {
        return CartItem.builder()
                .user(userDetailService.getCurrentUser())
                .product(productRepository.findById(cartItemRequest.getProductId()).get())
                .quantity(cartItemRequest.getQuantity())
                .build();
    }

    @Override
    public CartItemResponse getResponseFromEntity(CartItem cartItem) {
        return CartItemResponse.builder()
                .itemId(cartItem.getId())
                .productName(cartItem.getProduct().getName())
                .productPrice(NumberFormat.getInstance().format(cartItem.getProduct().getExportPrice())+"$")
                .quantity(cartItem.getQuantity())
                .amount(NumberFormat.getInstance().format(cartItem.getProduct().getExportPrice()*cartItem.getQuantity())+"$")
                .build();
    }
}