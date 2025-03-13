package Spring.Goods_Shop.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayResponseDto {

    private Long id;

    private String nickName;

    private String number;

    private String expPeriod;

    private String cvc;

    private boolean defaultCard;
}
