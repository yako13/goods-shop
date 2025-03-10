package Spring.Goods_Shop.dto.checkout;

import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.DeliveryCompany;
import Spring.Goods_Shop.enums.DeliveryState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutDetailsDto {

    private Long id;

    private CheckoutState checkoutState;

    private DeliveryCompany deliveryCompany;

    private String deliveryCode;

    private DeliveryState deliveryState;
}
