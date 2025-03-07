package Spring.Goods_Shop.productImage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    private final ProductImageManager productImageManager;

    public ProductImageUrlDto toProductImageUrlDto(List<ProductImage> productImages) {
        String productMainImageUrl = null;
        List<String> productSubImageUrl = new ArrayList<>();
        List<String> productDescImageUrl = new ArrayList<>();

        //이미지 타입에 따른 분류
        for (ProductImage productImage : productImages) {
            String imageUrl = productImageManager.createImageUrl(productImage.getImageFullName());
            switch (productImage.getImageType()) {
                case MAIN -> productMainImageUrl = imageUrl;
                case SUB ->
                        productSubImageUrl.add(imageUrl);
                case DESC ->
                        productDescImageUrl.add(imageUrl);
            }
        }

        return ProductImageUrlDto.builder()
                .mainImageUrl(productMainImageUrl)
                .subImageUrl(productSubImageUrl)
                .descriptionImageUrl(productDescImageUrl)
                .build();
    }
}
