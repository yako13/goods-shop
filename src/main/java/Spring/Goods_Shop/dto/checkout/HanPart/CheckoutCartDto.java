package Spring.Goods_Shop.dto.checkout.HanPart;

import Spring.Goods_Shop.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutCartDto {

    private Long CartId;                //카트 pk

    private String productName;         //상품 이름

    private ProductImage productImage;         //대표 상품 이미지

    private String productPrise;        //상품 가격

    private String productSumPrise;     //상품 합계 가격

    private int cartCnt;                //상품 개수


}
