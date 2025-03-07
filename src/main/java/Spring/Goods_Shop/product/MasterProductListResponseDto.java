package Spring.Goods_Shop.product;

import Spring.Goods_Shop.productImage.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterProductListResponseDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private int count;
    private String mainImagePath;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
