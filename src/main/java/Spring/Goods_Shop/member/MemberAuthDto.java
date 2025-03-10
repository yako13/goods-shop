package Spring.Goods_Shop.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthDto {

    @Pattern(regexp = "^[a-z]{1}[a-z0-9]{5,10}$"
            ,message = "아이디는 영문자와 숫자조합으로 5~11자리여야합니다.")
    @NotBlank(message = "아이디는 필수 정보입니다.")
    private String userId;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$"
            ,message = "비밀번호는 영대소문자, 숫자, 특수문자조합으로 8~15자리여야합니다.")
    @NotBlank(message = "비밀번호는 필수 정보입니다.")
    private String userPassword;

    @Pattern(regexp = "^[가-힣]{2,5}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$" , message = "이름은 한글 2~5자 또는 성과 이름을 구분하여 영문 2~10자리씩이여야 합니다.")
    @NotBlank(message = "이름은 필수 정보입니다.")
    private String name;

    @Pattern(regexp = "^(010|011|016|017|018|019)[0-9]{7,8}$",message = "휴대전화번호는 010, 011, 016, 017, 018, 019로 시작하며, 숫자 10~11자리여야 합니다.")
    @NotBlank(message = "휴대전화번호는 필수 정보입니다.")
    private String phoneNumber;

    private boolean termsAgreement;

    private boolean privacyAgreement;

}
