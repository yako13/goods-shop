package Spring.Goods_Shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Table(name = "checkout_details")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CheckoutDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkout_details_id")
    private Long id;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    @JoinColumn(name = "checkout_id", nullable = false)
    @ManyToOne
    private Checkout checkout;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @Comment("상품 가격")
    @Column(name = "checkout_detail_price", nullable = false)
    private BigDecimal checkoutDetailPrice;

    @Comment("상품 구매 수량")
    @Column(name = "checkout_detail_cnt", nullable = false)
    private int checkoutDetailCnt;
}
