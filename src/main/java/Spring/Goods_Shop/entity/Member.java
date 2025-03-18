package Spring.Goods_Shop.entity;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment(value = "회원PK")
    private Long id;

    @Column(nullable = false)
    @Comment(value = "회원 아이디")
    private String userId;

    @Column(nullable = true)
    @Comment(value = "회원 비밀번호")
    private String userPassword;

    @Column(nullable = true)
    @Comment(value = "회원 연락처")
    private String phoneNumber;

    @Column(nullable = false)
    @Comment(value = "회원 이름")
    private String name;

    @Column(nullable = true)
    @Comment(value = "OAuth2 제공자")
    private String provider;

    @Column(nullable = true)
    @Comment(value = "OAuth2 아이디")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment(value = "권한")
    private MemberRole role;

    @Column(nullable = false)
    @Comment(value = "약관 동의 여부")
    private boolean termsAgreement;

    @Column(nullable = false)
    @Comment(value = "개인정보 이용 동의 여부")
    private boolean privacyAgreement;

    @Column(nullable = true)
    @Comment(value = "탈퇴날짜")
    private LocalDateTime withdrawalAt;

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Checkout> checkoutList=new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Pay> payList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Delivery> deliveryList = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Cart> cartList=new ArrayList<>();

}