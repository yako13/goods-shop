package Spring.Goods_Shop.dto.product.Hanpart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCheckoutResDto {

    //상품 pk
    private Long productPk;

    //물건 개수
    private int productCnt;

}
