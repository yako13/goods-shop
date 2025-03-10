package Spring.Goods_Shop.product;

import Spring.Goods_Shop.enums.ProductCategory;
import Spring.Goods_Shop.productImage.ProductImage;
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
    private ProductCategory productCategory;
    private MultipartFile mainImage;
    private List<MultipartFile> subImage;
    private List<MultipartFile> descImage;


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
