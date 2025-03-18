package Spring.Goods_Shop.dto.checkout.HanPart;


import Spring.Goods_Shop.dto.product.Hanpart.ProductCheckoutResDto;
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
public class CheckoutPageResDto {


    // 상품을 주문/결제 페이지로 보여주기위한 dto
    private CheckoutCartDto CheckoutCartDto;

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

    //물건  가격 합 (정가)
    private String totalCartSumPrise;

    //총 결제 가격
    private String totalPaySumPrise;

    //맴버 pk
    private Long memberPk;

    //상품 pk와 상품 개수
    private ProductCheckoutResDto productCheckoutResDto;


}
