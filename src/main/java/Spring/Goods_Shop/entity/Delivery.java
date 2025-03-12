package Spring.Goods_Shop.entity;

import Spring.Goods_Shop.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


@Table(name = "delivery")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class Delivery extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @Column(nullable = true)
    @Comment("배송지명")
    private String name;

    @Column(name = "postal_code", nullable = false)
    @Comment("우편번호")
    private String postalCode;

    @Column( nullable = false)
    @Comment("주소")
    private String address;

    @Column( nullable = false)
    @Comment("상세주소")
    private String addressDetail;

    @Column(nullable = false)
    @Comment("수신자 이름")
    private String recipientName;

    @Column
    @Comment("수신자 연락처")
    private String recipientPhoneNumber;

    @Column(nullable = true)
    @Comment("배송 요청 사항")
    private String memo;

    @Column(nullable = false)
    @Comment("기본 배송지 여부")
    private boolean defaultDelivery;
}
