package Spring.Goods_Shop.dto.product;

import Spring.Goods_Shop.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailsResponseDto {

    private Long id;
    private String name;
    private int count;
    private BigDecimal price;
    private String totalPrice;
    private String productDescription;
    private ProductImage mainImage;
    private List<ProductImage> subImage;
    private List<ProductImage> descImage;
}
