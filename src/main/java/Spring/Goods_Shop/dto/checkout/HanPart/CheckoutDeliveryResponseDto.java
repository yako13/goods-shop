package Spring.Goods_Shop.dto.checkout.HanPart;

import Spring.Goods_Shop.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutDeliveryResponseDto {

    //배송지 pk
    private Long id;

    //맴버 클래스
    private Member member;

    //배송지 명
    private String name;

    //우편번호
    private String postalCode;

    //주소
    private String address;

    //상세주소
    private String addressDetail;

    //수신자 이름
    private String recipientName;

    //수신자 연락처
    private String recipientPhoneNumber;

    //배송 요청 사항
    private String memo;

    //기본 배송지 여부
    private boolean defaultDelivery;

    //장바구니 pk 리스트
    private List<Long> cartIdList;


}
