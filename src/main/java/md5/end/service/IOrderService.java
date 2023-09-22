package md5.end.service;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.request.OrderStatusUpdate;
import md5.end.model.dto.response.OrderDetailResponse;
import md5.end.model.dto.response.OrderResponse;
import md5.end.model.dto.response.ProductResponse;
import md5.end.model.entity.order.Order;
import md5.end.model.entity.order.OrderStatus;
import md5.end.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IOrderService extends IGenericService<OrderRequest, OrderResponse> {
    Page<OrderResponse> findAll(int page, int size);
    Page<OrderResponse> findByStatus(OrderStatus status,int page,int size) throws NotFoundException;
    Page<OrderResponse> findByDate(String date, int page,int size) throws NotFoundException;
    Page<OrderResponse> findByUserId(Long userId, int page,int size);


    OrderResponse findByIdWithUser(Long orderId,Long userId) throws NotFoundException;
    OrderDetailResponse findDetailById(Long id) throws NotFoundException;
    OrderDetailResponse findDetailWithUser(Long orderId,Long userId) throws NotFoundException;
    List<OrderResponse> findAllByUserId(Long id);
    OrderResponse findByStatus(OrderStatus orderStatus) throws NotFoundException;
    OrderResponse findByOrderDate(String orderDate) throws NotFoundException;
    OrderResponse updateStatus(Long orderId, OrderStatusUpdate orderStatusUpdate) throws NotFoundException, BadRequestException;
    OrderResponse delete(Long id) throws NotFoundException;


}
