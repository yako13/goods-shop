package Spring.Goods_Shop.product;

import Spring.Goods_Shop.productImage.ProductImageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductImageManager productImageManager;

    public Product toEntity(ProductRequestDto requestDto) {
        return Product.builder()
                .id(requestDto.getId())
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .productCategory(requestDto.getProductCategory())
                .count(requestDto.getCount())
                .productDescription(requestDto.getProductDescription())
                .build();
    }
}
