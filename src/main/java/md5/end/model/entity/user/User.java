package md5.end.model.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.order.Order;
import md5.end.model.entity.user.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Column (name = "full_name")
    private String fullName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(columnDefinition = "date")
    private String birthday;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String tel;

    private String address;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(columnDefinition = "timestamp", name = "created_at")
    private String createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(columnDefinition = "timestamp", name = "updated_at")
    private String updatedAt;


    private boolean status;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable (name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<CartItem> cartItems;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "user")
    private List<Order> orders;
}
