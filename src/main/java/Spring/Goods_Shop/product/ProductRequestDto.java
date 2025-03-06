package Spring.Goods_Shop.product;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.enums.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {

    private String name;
    private BigDecimal price;
    private int count;
    private String productDescription;
    private String productCategory;

    public ProductRequestDto(String name, BigDecimal price, int count, String productDescription, String productCategory) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
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
