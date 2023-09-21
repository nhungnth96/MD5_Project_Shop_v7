package md5.end.model.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import md5.end.model.entity.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String receiver;
    private String address;
    private String tel;
    private String note;
    private double total;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column (name = "order_date",columnDefinition = "timestamp")
    private String orderDate;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column (name = "shipping_date",columnDefinition = "timestamp")
    private LocalDateTime shippingDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderDetail> items = new ArrayList<>();


}