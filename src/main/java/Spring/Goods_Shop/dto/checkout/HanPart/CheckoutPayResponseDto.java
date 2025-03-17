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
public class CheckoutPayResponseDto {

    //결제 카드 pk
    private Long id;

    //맴버 정보
    private Member member;

    //카드 별칭
    private String nickname;

    //카드 번호
    private String number;

    //카드 유효기간
    private String expPeriod;

    //카드 CVC 코드
    private String cvc;

    //기본 결제 카드 여부
    private boolean defaultCard;

    //장바구니 pk 리스트
    private List<Long> cartIdList;


}
