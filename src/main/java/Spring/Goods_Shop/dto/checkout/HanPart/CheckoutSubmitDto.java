package Spring.Goods_Shop.dto.checkout.HanPart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutSubmitDto {

    //주문자 pk(주문 결제 페이지에 들어온 주문자랑 결제 버튼 누른 사람이 같은지 검사를 위해)
    private Long memberPk;

    //배송지 pk
    private Long deliveryPk;

    //결제 pk
    private Long payPk;

    //카트 pk
    private List<Long> cartPk;

//    --상품 상세페이지에서 주문 결제 페이지로 왔을때 사용하는 필드--
    //상품 pk
    private Long productPk;

    //상품 개수
    private int productCnt;


}
