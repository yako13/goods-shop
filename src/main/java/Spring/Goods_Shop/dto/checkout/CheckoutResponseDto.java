package Spring.Goods_Shop.dto.checkout;

import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto  {

    private Long id;

    private String checkoutCode;

    private String checkoutProductName;

    private int checkoutSize;

    private List<ProductListResponseDto> productList;

    private String checkoutName;

    private String checkoutDeliveryName;

    private String checkoutPostStep;

    private String checkoutStep;

    private String checkoutTotalPay;

    private String createdAt;

    private String modifiedAt;

}
