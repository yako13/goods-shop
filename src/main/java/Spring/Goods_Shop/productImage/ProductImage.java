package Spring.Goods_Shop.productImage;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Table(name = "product_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    @Comment("이미지 타입")
    private ImageType imageType;

    @Column(name = "image_path", nullable = false)
    @Comment("이미지 경로")
    private String imagePath;
}
