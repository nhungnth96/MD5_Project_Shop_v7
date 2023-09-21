package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.BrandRequest;
import md5.end.model.dto.request.CartItemRequest;
import md5.end.model.dto.request.CategoryRequest;
import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.response.CartItemResponse;
import md5.end.model.dto.response.CartItemResponse;

import md5.end.model.dto.response.OrderResponse;
import md5.end.model.entity.user.User;
import md5.end.security.principal.UserDetailService;
import md5.end.service.ICartItemService;
import md5.end.service.IOrderService;
import md5.end.service.impl.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")

public class CartItemController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserDetailService userDetailService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<String> checkout(
            @Valid
            @RequestBody OrderRequest orderRequest) throws NotFoundException, BadRequestException {
            cartItemService.checkout(orderRequest);
        return new ResponseEntity<>("Checkout successfully",HttpStatus.CREATED);

    }

    @PostMapping("")
    public ResponseEntity<CartItemResponse> add(
            @Valid
            @RequestBody CartItemRequest cartItemRequest) throws NotFoundException, BadRequestException {

        return new ResponseEntity<>(cartItemService.save(cartItemRequest), HttpStatus.CREATED);

    }

    @GetMapping("")
    public ResponseEntity<List<CartItemResponse>> getAll() {
        User user = userDetailService.getCurrentUser();
        return new ResponseEntity<>(cartItemService.findAllByUserId(user.getId()), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponse> getOne(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(cartItemService.findById(id), HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponse> edit(
            @Valid
            @RequestBody CartItemRequest cartItemRequest,
            @PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(cartItemService.update(cartItemRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartItemResponse> delete(@PathVariable Long id) throws NotFoundException {
        return new ResponseEntity<>(cartItemService.deleteById(id), HttpStatus.OK);

    }


}