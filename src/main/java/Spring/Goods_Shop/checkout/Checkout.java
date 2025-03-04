package Spring.Goods_Shop.checkout;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.ShipmentState;
import Spring.Goods_Shop.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Table(name = "checkout")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Checkout extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkout_id")
    private Long id;

//    @JoinColumn(name = "member_id", nullable = false)
//    @OneToMany
//    private Member member;

    @Comment("주문자 명")
    @Column(name = "checkout_name", nullable = false)
    private String checkoutName;

    @Comment("주문자 배송지명")
    @Column(name = "checkout_delivery_name", nullable = false)
    private String checkoutDeliveryName;

    @Comment("주문자 우편번호")
    @Column(name = "checkout_zip_code", nullable = false)
    private String checkoutZipCode;

    @Comment("주문자 도로명 주소")
    @Column(name = "checkout_add_city", nullable = false)
    private String checkoutAddCity;

    @Comment("결제한 카드명")
    @Column(name = "checkout_card_name", nullable = false)
    private String checkoutCardName;

    @Comment("결제한 카드 번호")
    @Column(name = "checkout_card_num", nullable = false)
    private String checkoutCardNum;

    @Comment("결제한 카드 cvc")
    @Column(name = "checkout_card_cvc", nullable = false)
    private String checkoutCardCvc;

    @Comment("택배사 명")
    @Column(name = "checkout_post_name", nullable = false)
    private String checkoutPostName;

    @Comment("배송 상태")
    @Column(name = "checkout_post_step", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentState checkoutPostStep;

    @Comment("주문 상태")
    @Column(name = "checkout_step", columnDefinition = "VARCHAR(50)", nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckoutState checkoutStep;

    @Comment("총 결제 금액")
    @Column(name = "checkout_total_pay", nullable = false)
    private String checkoutTotalPay;
}
