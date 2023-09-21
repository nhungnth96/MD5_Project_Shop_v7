package md5.end.model.entity.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.order.OrderDetail;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Category;
import md5.end.model.entity.user.Role;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;



    @Column(name = "import_price")
    private double importPrice;



    @Column(name = "export_price")
    private double exportPrice;

    private int rating;

    @Lob
    private String description;


    private int stock;

    private int status; //  -- 1: còn hàng, 2: hết hàng, 3: ngừng kinh doanh


    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "product")
    private List<ProductSpec> specifications;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,mappedBy = "product")
    private List<ProductImage> images;



}
