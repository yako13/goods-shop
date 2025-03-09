package Spring.Goods_Shop.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto {

    private Long id;

    private String checkoutProductName;

    private String checkoutName;

    private String checkoutDeliveryName;

    private String checkoutZipCode;

    private String checkoutAddress;

    private String checkoutDeliveryMemo;

    private String checkoutCardName;

    private String checkoutCardNum;

    private String checkoutCardCvc;

    private String checkoutExpPeriod;

    private String checkoutPostName;

    private String checkoutPostStep;

    private String checkoutStep;

    private String checkoutTotalPay;

    private String createdAt;

    private String modifiedAt;
}
