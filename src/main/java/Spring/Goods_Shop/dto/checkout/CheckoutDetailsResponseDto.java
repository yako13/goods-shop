package Spring.Goods_Shop.dto.checkout;

import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutDetailsResponseDto {

    //주문PK
    private Long id;

    //주문상태
    private String checkoutState;

    //배송업체
    private String deliveryCompany;

    //배송상태
    private String deliveryState;

    //운송장번호
    private String deliveryCode;

    //주문번호
    private String checkoutCode;

    //주문날짜
    private String checkoutDate;

    //총 결제 금액
    private String totalPay;

    //총 상품 금액
    private String totalProductCost;

    //배송비
    private String deliveryCost;

    //카드 번호
    private String cardCode;

    //상품 리스트
    private List<ProductListResponseDto> productList;

    //주문자명
    private String ordererName;

    //주문자의 아이디 또는 이메일주소
    private String ordererId;

    //주문자 연락처
    private String ordererPhoneNumber;

    //수취인명
    private String recipientName;

    //수취인 연락처
    private String recipientPhoneNumber;

    //배송지 주소
    private String address;

    //배송 메모
    private String deliveryMemo;

}
