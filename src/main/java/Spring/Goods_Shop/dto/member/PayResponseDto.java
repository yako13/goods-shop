package Spring.Goods_Shop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayResponseDto {
    private Long memberId;

    private String nickName;

    private String number;

    private String expPeriod;

    private String cvc;

    private boolean defaultCard;
}
