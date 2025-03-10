package Spring.Goods_Shop.product;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.enums.ProductCategory;
import Spring.Goods_Shop.productImage.ProductImage;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    @Comment("상품명")
    private String name;

    @Column(nullable = false)
    @Comment("상품 가격")
    private BigDecimal price;

    @Column(nullable = false)
    @Comment("재고 수량")
    private int count;

    @Column(name = "product_description", nullable = true)
    @Comment("상품에 관한 설명")
    private String productDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", columnDefinition = "VARCHAR(50)", nullable = false)
    @Comment("제품 분류")
    private ProductCategory productCategory;;

    @JoinColumn(name = "product_image")
    @Comment("상품의 대표 이미지")
    @OneToOne
    private ProductImage productImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImageList = new ArrayList<>();

    @Builder
    public Product(Long id, String name, BigDecimal price, int count, String productDescription, ProductCategory productCategory, ProductImage productImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.productCategory = productCategory;
        this.productDescription = productDescription;
        this.productImage = productImage;
    }

    public void update(ProductRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
        this.count = requestDto.getCount();
        this.productCategory = requestDto.getProductCategory();
        this.productDescription = requestDto.getProductDescription();
    }
}