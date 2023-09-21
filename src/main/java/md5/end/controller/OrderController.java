package md5.end.controller;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.request.OrderStatusUpdate;
import md5.end.model.dto.request.ProductRequest;
import md5.end.model.dto.response.OrderDetailResponse;
import md5.end.model.dto.response.OrderResponse;
import md5.end.model.entity.order.OrderStatus;
import md5.end.model.entity.user.RoleName;
import md5.end.model.entity.user.User;
import md5.end.security.principal.UserDetailService;
import md5.end.service.IOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private UserDetailService userDetailService;
    @GetMapping("")

    public ResponseEntity<List<OrderResponse>> getAll() {
        User user = userDetailService.getCurrentUser();
        if(user.getRoles().size()==1) {
            return new ResponseEntity<>(orderService.findAllByUserId(user.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(orderService.findAll(), HttpStatus.OK);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOne(@PathVariable Long id) throws NotFoundException {
        User user = userDetailService.getCurrentUser();
        if(user.getRoles().size()==1) {
            return new ResponseEntity<>(orderService.findByIdWithUser(id,user.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(orderService.findById(id), HttpStatus.OK);
        }
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<OrderDetailResponse> getDetail(@PathVariable Long id) throws NotFoundException {
        User user = userDetailService.getCurrentUser();
        if(user.getRoles().size()==1) {
            return new ResponseEntity<>(orderService.findDetailWithUser(id,user.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(orderService.findDetailById(id), HttpStatus.OK);
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    public ResponseEntity<OrderResponse> updateStatus(
            @Valid
            @PathVariable Long id,
            @RequestBody OrderStatusUpdate statusCode) throws NotFoundException, BadRequestException {
        return new ResponseEntity<>(orderService.updateStatus(id,statusCode), HttpStatus.OK);
    }


}
