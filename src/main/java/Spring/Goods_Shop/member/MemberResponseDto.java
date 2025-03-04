package Spring.Goods_Shop.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {

    private String userId;

    private String userPassword;

    private String name;

    private String phoneNumber;

    private boolean termsAgreement;

    private boolean privacyAgreement;
}
