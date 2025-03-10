package Spring.Goods_Shop.productImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductImageUrlDto {

    private final String mainImageUrl;

    private final List<String> subImageUrl;

    private final List<String> descImageUrl;
}
