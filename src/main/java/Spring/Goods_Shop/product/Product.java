package Spring.Goods_Shop.product;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.cart.Cart;
import Spring.Goods_Shop.checkout.Checkout;
import Spring.Goods_Shop.checkoutDetails.CheckoutDetails;
import Spring.Goods_Shop.productImage.ProductImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "product")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    @Comment("상품명")
    private String name;

    @Column(nullable = false)
    @Comment("상품 가격")
    private BigDecimal price;

    @Column(nullable = false)
    @Comment("재고 수량")
    private int count;

    @Column(name = "product_description", nullable = true)
    @Comment("상품에 관한 설명")
    private String productDescription;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductImage> productImageList=new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Cart> cartList=new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CheckoutDetails> checkoutDetailsList=new ArrayList<>();
}
