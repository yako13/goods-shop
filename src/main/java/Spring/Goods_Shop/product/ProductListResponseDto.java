package Spring.Goods_Shop.product;

import Spring.Goods_Shop.productImage.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private String mainImagePath;
}
