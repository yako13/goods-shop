package Spring.Goods_Shop.dto.product;

import Spring.Goods_Shop.enums.ProductCategory;
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
    private String productCategory;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
