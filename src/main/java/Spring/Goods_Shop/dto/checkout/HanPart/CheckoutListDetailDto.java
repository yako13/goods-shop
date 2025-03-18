package Spring.Goods_Shop.dto.checkout.HanPart;

import Spring.Goods_Shop.entity.Checkout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutListDetailDto {

    //주문 정보
    private Checkout checkout;

    //주문 상세의 상품에 원단위 붙여주고 상품의 총가격 (상품가격 * 상품개수)를 넣어주기위한 리스트
    private List<CheckoutCartDto> CheckoutCartDtoList;

    //총 결제 금액
    private String CheckoutTotalPay;

    //수신자 전화번호
    private String checkoutPhoneNumber;

    //배송비
    private String checkoutDeliveryCost;

    //택배사명
    private String DeliveryCompany;

    //배송 상태
    private String checkoutPostStep;

    //주문 상태
    private String checkoutStep;




}
