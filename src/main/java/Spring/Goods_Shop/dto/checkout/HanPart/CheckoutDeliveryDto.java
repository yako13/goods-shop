package Spring.Goods_Shop.dto.checkout.HanPart;

import Spring.Goods_Shop.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutDeliveryDto {

    //배송지 pk

    private Long id;

    //맴버 클래스
    private Member member;

    //배송지 명
    @Pattern(regexp = "^[가-힣0-9]{2,10}|[a-zA-Z0-9]{2,10}$", message = "배송지 별칭은 한글과 숫자조합으로 2~10자 또는 영문과 숫자 조합으로 2~10자리여야 합니다.")
    @NotBlank(message = "배송지 별칭은 필수 정보입니다.")
    private String name;

    //우편번호
    @Pattern(regexp = "^[0-9]{5}$", message = "우편번호는 5자리의 숫자여야합니다.")
    @NotBlank(message = "우편번호는 필수 정보입니다.")
    private String postalCode;

    //주소
    @Pattern(regexp = "^[\\s\\S]{10,100}$", message = "주소는 10~100자리까지만 가능합니다.")
    @NotBlank(message = "주소는 필수 정보입니다.")
    private String address;

    //상세주소
    @Pattern(regexp = "^[\\s\\S]{0,100}$", message = "상세주소는 100자 이내입니다.")
    private String addressDetail;

    //수신자 이름
    @Pattern(regexp = "^[가-힣]{2,5}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$", message = "받는분 성함은 한글 2~5자 또는 성과 이름을 구분하여 영문 2~10자리씩이여야 합니다.")
    @NotBlank(message = "받는분 성함은 필수 정보입니다.")
    private String recipientName;

    //수신자 연락처
    @Pattern(regexp = "^(010|011|016|017|018|019)[0-9]{7,8}$", message = "받는분 연락처는 010, 011, 016, 017, 018, 019로 시작하며, 숫자 10~11자리여야 합니다.")
    @NotBlank(message = "받는분 연락처는 필수 정보입니다.")
    private String recipientPhoneNumber;

    //배송 요청사항
    @Pattern(regexp = "^[\\s\\S]{0,50}$", message = "배송 요청사항은 50자 이내입니다.")
    private String memo;

    //기본 배송지 여부
    private boolean defaultDelivery;

    //상품 pk
    private Long productPk;

    //상품 개수
    private int productCnt;

    //장바구니 pk 리스트
    private List<Long> cartIdList;


}
