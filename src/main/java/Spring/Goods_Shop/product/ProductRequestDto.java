package Spring.Goods_Shop.product;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.enums.ProductCategory;
import Spring.Goods_Shop.productImage.ProductImage;
import Spring.Goods_Shop.productImage.ProductImageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private int count;
    private String productDescription;
    private String productCategory;
    private MultipartFile mainImage;
    private List<MultipartFile> subImage;
    private List<MultipartFile> descImage;

    public ProductRequestDto(Long id, String name, BigDecimal price, int count, String productDescription, String productCategory,
                             MultipartFile mainImage, List<MultipartFile> subImage, List<MultipartFile> descImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
        this.mainImage = mainImage;
        this.subImage = subImage;
        this.descImage = descImage;
    }

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .count(count)
                .productDescription(productDescription)
                .productCategory(productCategory)
                .build();
    }
}
