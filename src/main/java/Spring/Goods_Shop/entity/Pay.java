package Spring.Goods_Shop.entity;

import Spring.Goods_Shop.base.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "pay")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pay extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @Column(nullable = true)
    @Comment("카드 별침")
    private String nickname;

    @Column(nullable = false)
    @Comment("카드 번호")
    private String number;

    @Column(name = "exp_period")
    @Comment("카드 유효기간")
    private String expPeriod;

    @Column(nullable = false)
    @Comment("카드 CVC 코드")
    private String cvc;

    @Column(nullable = false)
    @Comment("기본 결제 카드 여부")
    private boolean defaultCard;
}
