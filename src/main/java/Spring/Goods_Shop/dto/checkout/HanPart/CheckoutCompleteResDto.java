package Spring.Goods_Shop.dto.checkout.HanPart;

import Spring.Goods_Shop.entity.Checkout;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutCompleteResDto {

    //주문 엔티티를 그대로 담는다
    private Checkout checkout;

    //카드 뒷자리 *를 위해 카드번호를 따로 담는다
    private String checkoutCardNum;

    //전화번호 하이폰 달아주기 위해 따로 담는다
    private String checkoutPhoneNumber;


}
