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

    private Long memberPK;

    private String userId;

    private String name;

    private String phoneNumber;

    private String provider;
}
