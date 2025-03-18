package Spring.Goods_Shop.dto.checkout.HanPart;

import Spring.Goods_Shop.entity.Delivery;
import Spring.Goods_Shop.entity.Pay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutCartPageResponseDto {

    //장바구니에 있는 물품들을 주문/결제 페이지로 보여주기위한 dto 리스트
    private List<CheckoutCartDto> CheckoutCartDtoList;

    //배송지 리스트
    private List<CheckoutDeliveryResponseDto> DeliveryList;

    //결제카드 리스트
    private List<CheckoutPayResponseDto> PayList;

    //기본 배송지
    private Delivery defaultDelivery;

    //기본 결제 카드
    private Pay defaultPay;

    //배송비
    private String DeliveryCost;

    //장바구니 물건  가격 합 (정가)
    private String totalCartSumPrise;

    //총 결제 가격
    private String totalPaySumPrise;

    //맴버 pk
    private Long memberPk;


}
