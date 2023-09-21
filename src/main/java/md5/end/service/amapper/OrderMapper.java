package md5.end.service.amapper;


import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.response.ItemResponse;
import md5.end.model.dto.response.OrderDetailResponse;
import md5.end.model.dto.response.OrderResponse;
import md5.end.model.entity.order.*;
import md5.end.model.entity.product.Product;
import md5.end.repository.*;
import md5.end.security.principal.UserDetailService;
import md5.end.service.impl.OrderService;
import md5.end.service.impl.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class OrderMapper implements IGenericMapper<Order, OrderRequest, OrderResponse> {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IShippingRepository shippingRepository;
    @Autowired
    private IOrderDetailRepository orderDetailRepository ;
    @Autowired
    private ShippingService shippingService;
    @Override
    public Order getEntityFromRequest(OrderRequest orderRequest) throws NotFoundException, BadRequestException {
        Order order = new Order();
        order.setUser(userDetailService.getCurrentUser());
        order.setReceiver(orderRequest.getReceiver());
        order.setAddress(orderRequest.getAddress());
        order.setTel(orderRequest.getTel());
        order.setNote(orderRequest.getNote());
        order.setOrderDate(LocalDateTime.now().toString());
        order.setPayment(paymentRepository.findById(orderRequest.getPaymentId()).get());
        order.setStatus(OrderStatus.PENDING);
        order.setActive(true);
        double total = 0;
        List<CartItem> cartItems = order.getUser().getCartItems();
        if(cartItems.isEmpty()){
            throw new BadRequestException("Empty cart");
        }
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).get();
            if(product.getStock()<cartItem.getQuantity()){
                throw new BadRequestException("The product in stock only have "+product.getStock()+" please reduce quantity.");
            } else {
                product.setStock(product.getStock()-cartItem.getQuantity());
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(cartItem.getProduct());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(orderDetail.getProduct().getExportPrice());
            orderDetail.setAmount(orderDetail.getProduct().getExportPrice() * orderDetail.getQuantity());
            orderDetail.setOrder(order);
            order.getItems().add(orderDetail);
            total += orderDetail.getAmount();
        }
        order.setTotal(total);
        if(orderRequest.getShippingId()==1){
            order.setShipping(shippingService.findByType(ShippingType.ECONOMY));
        } else if (orderRequest.getShippingId()==2) {
            order.setShipping(shippingService.findByType(ShippingType.FAST));
        }  else if (orderRequest.getShippingId()==3) {
            order.setShipping(shippingService.findByType(ShippingType.EXPRESS));
        } else {
            throw new NotFoundException("Cannot find this shipping type");
        }
        order.setTotal(order.getTotal()+order.getShipping().getPrice());
        return order;
    }

    @Override
    public OrderResponse getResponseFromEntity(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(order.getId());
        orderResponse.setBuyer(order.getUser().getFullName());
        orderResponse.setNote(order.getNote());
        orderResponse.setTotal(NumberFormat.getInstance().format(order.getTotal()) + "$");
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setStatus(order.getStatus().name());
        orderResponse.setPayment(order.getPayment().getType().toString());
        orderResponse.setShipping(order.getShipping().getType().toString());
        return orderResponse;
    }

    public OrderDetailResponse getDetailResponseFromEntity(Order order) {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setBuyerName(order.getUser().getFullName());
        orderDetailResponse.setReceiver(order.getReceiver());
        orderDetailResponse.setAddress(order.getAddress());
        orderDetailResponse.setTel(order.getTel());
        orderDetailResponse.setNote(order.getNote());
        orderDetailResponse.setTotal(NumberFormat.getInstance().format(order.getTotal()) + "$");
        orderDetailResponse.setShippingFee(NumberFormat.getInstance().format(order.getShipping().getType().getPrice()) + "₫");
        orderDetailResponse.setSubTotal(NumberFormat.getInstance().format(order.getTotal()+order.getShipping().getType().getPrice()) + "₫");
        orderDetailResponse.setOrderDate(order.getOrderDate());
        orderDetailResponse.setStatus(order.getStatus().name());
        orderDetailResponse.setPayment(order.getPayment().getType().toString());
        orderDetailResponse.setShipping(order.getShipping().getType().toString());

        List<ItemResponse> itemResponseList = new ArrayList<>();
        for (OrderDetail item : order.getItems()) {
            ItemResponse itemResponse = new ItemResponse();
            itemResponse.setProductName(item.getProduct().getName());
            itemResponse.setPrice(NumberFormat.getInstance().format(item.getProduct().getExportPrice())+"$");
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setAmount(NumberFormat.getInstance().format(item.getAmount())+"$");
            itemResponseList.add(itemResponse);
        }
        orderDetailResponse.setItems(itemResponseList);
        return orderDetailResponse;
    }
}
