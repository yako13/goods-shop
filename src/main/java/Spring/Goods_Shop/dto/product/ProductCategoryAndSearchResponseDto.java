package Spring.Goods_Shop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryAndSearchResponseDto {

    private Long id;
    private String name;
    private String price;
    private String mainImagePath;
    private String productCategory;
    private int count;

}
