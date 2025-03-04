package Spring.Goods_Shop.delivery;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "delivery")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Delivery extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_name")
    private Long id;

    @JoinColumn(name = "member_id")
    @OneToMany
    private Member member;

    @Column(nullable = true)
    @Comment("배송지명")
    private String name;

    @Column(name = "postal_code", nullable = false)
    @Comment("우편번호")
    private String postalCode;

    @Column(name = "street_address", nullable = false)
    @Comment("도로명 주소")
    private String streetAddress;

    @Column(name = "lot_number_address", nullable = false)
    @Comment("구주소")
    private String lotNumberAddress;

    @Column(name = "detail_address", nullable = false)
    @Comment("상세주소")
    private String detailAddress;

    @Column(name = "recipient_name", nullable = false)
    @Comment("수신자 이름")
    private String recipientName;

    @Column(name = "recipient_phone_number")
    @Comment("수신자 연락처")
    private String recipientPhoneNumber;

    @Column(nullable = true)
    @Comment("배송 요청 사항")
    private String memo;
}
