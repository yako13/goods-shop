package Spring.Goods_Shop.dto.product;

import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.enums.ProductCategory;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "상품명은 필수 입력사항입니다.")
    @Pattern(regexp = "^(?=\\S)(.*\\S)?$", message = "상품명의 시작과 끝은 공백이 될 수 없습니다.")
    @Size(max = 30, message = "상품명은 최대 30자까지 입력가능합니다.")
    private String name;

    @NotNull(message = "판매 가격은 필수 입력사항입니다.")
    @PositiveOrZero(message = "판매 가격은 100원 이상 이여야 합니다.")
    private BigDecimal price;

    @NotNull(message = "재고 수량 필수 입력사항입니다.")
    @PositiveOrZero(message = "재고 수량은 0미만 일수 없습니다.")
    private int count;

    @Size(max = 200, message = "상품 설명은 최대 200자까지 입력가능합니다.")
    private String productDescription;

    @NotNull(message = "카테고리는 필수 입력사항입니다.")
    private ProductCategory productCategory;

    @NotNull(message = "상품 메인 이미지는 필수 입력사항입니다.")
    private MultipartFile mainImage;

    @Size(max = 3, message = "상품 서브 이미지는 최대 3장까지 저장 가능합니다.")
    private List<MultipartFile> subImage;

    @Size(max = 5, message = "상품 설명 이미지는 최대 5장까지 저장 가능합니다.")
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
