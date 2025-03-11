package Spring.Goods_Shop.inter;

import Spring.Goods_Shop.dto.product.MasterProductListResponseDto;
import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final ProductImageManager productImageManager;

    public MasterProductListResponseDto toMasterProductListResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        return MasterProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().setScale(0, RoundingMode.FLOOR))
                .count(product.getCount())
                .mainImagePath(productMainImagePath)
                .category(product.getProductCategory().name())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }
    public ProductListResponseDto toProductListResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }
        return ProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().setScale(0, RoundingMode.FLOOR))
                .mainImagePath(productMainImagePath)
                .build();
    }
}
