package Spring.Goods_Shop.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 찾기, 피드백 Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;

    private String userId;

    private String userPassword;

    private String name;

    private String phoneNumber;

    private String provider;

}
