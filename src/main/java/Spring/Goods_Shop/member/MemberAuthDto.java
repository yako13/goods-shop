package Spring.Goods_Shop.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입, 계정 수정 Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthDto {
    private String userId;

    private String userPassword;

    private String name;

    private String phoneNumber;

    private boolean termsAgreement;

    private boolean privacyAgreement;

}
