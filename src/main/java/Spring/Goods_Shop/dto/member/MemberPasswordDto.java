package Spring.Goods_Shop.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberPasswordDto {

    private String userId;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$"
            ,message = "비밀번호는 영대소문자, 숫자, 특수문자조합으로 8~15자리여야합니다.")
    @NotBlank(message = "비밀번호는 필수 정보입니다.")
    private String userPassword;
}
