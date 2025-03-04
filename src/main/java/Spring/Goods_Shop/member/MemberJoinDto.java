package Spring.Goods_Shop.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinDto {

    private String userId;

    private String userPassword;

    private String name;

    private String phoneNumber;

    private boolean termsAgreement;

    private boolean privacyAgreement;

}
