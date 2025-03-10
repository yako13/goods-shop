package Spring.Goods_Shop.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto {

    private Long id;

    private String checkoutCode;

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

    private String checkoutDeliveryCompany;

    private String checkoutPostStep;

    private String checkoutStep;

    private String checkoutTotalPay;

    private String createdAt;

    private String modifiedAt;
}
