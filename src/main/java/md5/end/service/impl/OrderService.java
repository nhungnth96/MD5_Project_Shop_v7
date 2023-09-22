package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.request.OrderStatusUpdate;
import md5.end.model.dto.response.OrderDetailResponse;
import md5.end.model.dto.response.OrderResponse;
import md5.end.model.dto.response.ProductShopResponse;
import md5.end.model.entity.order.*;
import md5.end.model.entity.product.Product;
import md5.end.model.entity.user.User;
import md5.end.repository.*;
import md5.end.security.principal.UserDetailService;
import md5.end.service.ICartItemService;
import md5.end.service.IOrderService;
import md5.end.service.IShippingService;
import md5.end.service.IUserService;
import md5.end.service.amapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService implements IOrderService {
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    IProductRepository productRepository;
    @Override
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> orderMapper.getResponseFromEntity(order))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrderResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        Page<OrderResponse> orderResponsePage = orderPage.map(order -> orderMapper.getResponseFromEntity(order));
        return orderResponsePage;
    }

    @Override
    public Page<OrderResponse> findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> orderPage = orderRepository.findByUserId(userId,pageable);
        Page<OrderResponse> orderResponsePage = orderPage.map(order -> orderMapper.getResponseFromEntity(order));
        return orderResponsePage;
    }

    @Override
    public Page<OrderResponse> findByDate(String date, int page, int size) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> orderPage = orderRepository.findByOrderDate(date,pageable);
        Page<OrderResponse> orderResponsePage = orderPage.map(order -> orderMapper.getResponseFromEntity(order));
        return orderResponsePage;
    }

    @Override
    public Page<OrderResponse> findByStatus(OrderStatus status, int page, int size) throws NotFoundException {
        Pageable pageable = PageRequest.of(page,size);
        Page<Order> orderPage = orderRepository.findByStatus(status,pageable);
        Page<OrderResponse> orderResponsePage = orderPage.map(order -> orderMapper.getResponseFromEntity(order));
        return orderResponsePage;
    }

    @Override
    public List<OrderResponse> findAllByUserId(Long id) {
        List<Order> orders = orderRepository.findAllByUserId(id);
        return orders.stream()
                .map(order -> orderMapper.getResponseFromEntity(order))
                .collect(Collectors.toList());
    }



    @Override
    public OrderResponse findByStatus(OrderStatus orderStatus) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findByStatus(orderStatus);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Status "+orderStatus+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse findByOrderDate(String orderDate) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findByOrderDate(orderDate);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order of date "+orderDate+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse findById(Long id) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order's id "+id+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderDetailResponse findDetailById(Long id) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order's id "+id+" not found.");
        }
        return orderMapper.getDetailResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse findByIdWithUser (Long orderId,Long userId) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order's id "+orderId+" not found.");
        }
        if(!orderOptional.get().getUser().getId().equals(userId)){
            throw new NotFoundException("Your orders don't have id "+orderId);
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderDetailResponse findDetailWithUser(Long orderId,Long userId) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order's id "+orderId+" not found.");
        }
        if(!orderOptional.get().getUser().getId().equals(userId)){
            throw new NotFoundException("Your orders don't have id "+orderId);
        }
        return orderMapper.getDetailResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse save(OrderRequest orderRequest) throws BadRequestException, NotFoundException {
        Order order = orderRepository.save(orderMapper.getEntityFromRequest(orderRequest));
        return orderMapper.getResponseFromEntity(order);
    }


    @Override
    public OrderResponse updateStatus(Long orderId, OrderStatusUpdate orderStatusUpdate) throws NotFoundException, BadRequestException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException("Order's id "+orderId+" not found.");
        }
        Order order = orderOptional.get();
        order.setId(orderId);
        if(order.getStatus()==OrderStatus.PENDING) {
            switch (orderStatusUpdate.getStatusCode()) {
                case 1:
                    order.setStatus(OrderStatus.PROCESSING);
                    break;
                case 2:
                    order.setStatus(OrderStatus.SHIPPING);
                    break;
                case 3:
                    order.setStatus(OrderStatus.DELIVERED);
                    break;
                case 4:
                    order.setStatus(OrderStatus.CANCELLED);
                    break;
                default:
                    throw new BadRequestException("Status's value must be 1-4."
                            + "\n" +
                            "1: Processing" + "\n" +
                            "2: Shipping" + "\n" +
                            "3: Delivered" + "\n" +
                            "4: Cancel");
            }
        } else {
            throw new BadRequestException("Can't change status");
        }
        if(order.getStatus()==OrderStatus.CANCELLED) {
            for (OrderDetail item : order.getItems()) {
                Product product = productRepository.findById(item.getProduct().getId()).get();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }
        return orderMapper.getResponseFromEntity(orderRepository.save(order));
    }


    @Override
    public OrderResponse deleteById(Long id) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException("Order's id "+id+" not found.");
        }
        Order order = orderOptional.get();
        order.setActive(false);
        return orderMapper.getResponseFromEntity(orderRepository.save(order));
    }

    @Override
    public OrderResponse update(OrderRequest orderRequest, Long id) throws NotFoundException {
        return null;
    }
    @Override
    public OrderResponse delete(Long id) throws NotFoundException {
        return null;
    }


}
