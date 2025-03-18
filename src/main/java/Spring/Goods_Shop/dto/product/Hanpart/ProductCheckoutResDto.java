package Spring.Goods_Shop.dto.product.Hanpart;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductCheckoutResDto {

    //상품 pk
    private Long productPk;

    //물건 개수
    private int productCnt;

}
