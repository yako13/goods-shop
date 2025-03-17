package Spring.Goods_Shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Table(name = "cart")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne
    private Member member;

    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne
    private Product product;

    @Column(name = "cart_cnt", nullable = false)
    @Comment("구매수량")
    private int cartCnt;
}
