package Spring.Goods_Shop.entity;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.DeliveryCompany;
import Spring.Goods_Shop.enums.DeliveryState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Table(name = "checkout")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Checkout extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkout_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @Column(nullable = false)
    @Comment("주문번호")
    private String checkoutCode;

    @Comment("주문자 명")
    @Column(name = "checkout_name", nullable = false)
    private String checkoutName;

    @Comment("주문자 배송지명")
    @Column(name = "checkout_delivery_name", nullable = false)
    private String checkoutDeliveryName;

    @Comment("주문자 우편번호")
    @Column(name = "checkout_zip_code", nullable = false)
    private String checkoutZipCode;

    @Comment("주문자 도로명,지번 주소 + 상세주소")
    @Column(name = "checkout_address", nullable = false)
    private String checkoutAddress;

    @Comment("주문자 배송 메모")
    @Column(name = "checkout_delivery_memo", nullable = true)
    private String checkoutDeliveryMemo;

    @Comment("결제한 카드명")
    @Column(name = "checkout_card_name", nullable = false)
    private String checkoutCardName;

    @Comment("결제한 카드 번호")
    @Column(name = "checkout_card_num", nullable = false)
    private String checkoutCardNum;

    @Comment("결제한 카드 cvc")
    @Column(name = "checkout_card_cvc", nullable = false)
    private String checkoutCardCvc;

    @Comment("결제한 카드 유효기간")
    @Column( nullable = false)
    private String checkoutExpPeriod;

    @Comment("택배사 명")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryCompany checkoutDeliveryCompany;

    @Comment("배송 상태")
    @Column(name = "checkout_post_step", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryState checkoutPostStep;

    @Column
    @Comment("운송장번호")
    private String checkoutDeliveryCode;

    @Comment("주문 상태")
    @Column(name = "checkout_step", nullable = false)
    @Enumerated(EnumType.STRING)
    private CheckoutState checkoutStep;

    @Comment("총 결제 금액")
    @Column(name = "checkout_total_pay", nullable = false)
    private BigDecimal checkoutTotalPay;

    @Comment("배송비")
    @Column
    private BigDecimal checkoutDeliveryCost;

    @Comment("주문자 연락처")
    @Column
    private String checkoutPhoneNumber;

    @OneToMany(mappedBy = "checkout",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CheckoutDetails> checkoutDetailsList=new ArrayList<>();

}
