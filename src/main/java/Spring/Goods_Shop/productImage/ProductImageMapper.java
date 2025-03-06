package Spring.Goods_Shop.productImage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static Spring.Goods_Shop.enums.ImageType.*;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    private final ImageHandler imageHandler;

    public ProductImageUrlDto toProductImageUrlDto(List<ProductImage> productImages) {
        String productMainImageUrl = null;
        List<String> productSubImageUrl = new ArrayList<>();
        List<String> productDescImageUrl = new ArrayList<>();

        //이미지 타입에 따른 분류
        for (ProductImage productImage : productImages) {
            switch (productImage.getImageType()) {
                case MAIN -> productMainImageUrl = imageHandler.createImageUrl(productImage.getImageFullName());
                case SUB ->
                        productSubImageUrl.add(imageHandler.createImageUrl(productImage.getImageFullName()));
                case DESC ->
                        productDescImageUrl.add(imageHandler.createImageUrl(productImage.getImageFullName()));
            }
        }

        return ProductImageUrlDto.builder()
                .mainImageUrl(productMainImageUrl)
                .subImageUrl(productSubImageUrl)
                .descriptionImageUrl(productDescImageUrl)
                .build();
    }
}
