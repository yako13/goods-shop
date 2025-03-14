package Spring.Goods_Shop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayDto {

    private Long memberId;

    @Pattern(regexp = "^[가-힣0-9]{2,10}|[a-zA-Z0-9]{2,10}$" , message = "카드 별칭은 한글과 숫자조합으로 2~10자 또는 영문과 숫자 조합으로 2~10자리여야 합니다.")
    @NotBlank(message = "카드 별칭은 필수 정보입니다.")
    private String nickName;

    private List<@Pattern(regexp = "^[0-9]{4}$"
            ,message = "카드번호는 숫자 네자리씩이어야 합니다.")String> cardNum;

    private List<@Pattern(regexp = "^[0-9]{2}$"
            ,message = "카드 유효기간은 숫자 두자리씩이어야 합니다.")String> expPeriod;

    @Pattern(regexp = "^[0-9]{3}$"
            ,message = "카드 cvc는 숫자 세자리여야 합니다.")
    @NotBlank(message = "카드 cvc는 필수 정보입니다.")
    private String cvc;

    private boolean defaultCard;
}
