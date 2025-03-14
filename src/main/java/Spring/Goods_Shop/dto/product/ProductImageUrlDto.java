package Spring.Goods_Shop.dto.product;

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
