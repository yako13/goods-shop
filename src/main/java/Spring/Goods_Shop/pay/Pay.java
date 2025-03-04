package Spring.Goods_Shop.pay;

import Spring.Goods_Shop.member.Member;
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
public class Pay {

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
}
