package Spring.Goods_Shop.productImage;

import Spring.Goods_Shop.base.BaseTime;
import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.UUID;

@Table(name = "product_image")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductImage extends BaseTime {

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

    @Column(name = "image_name", nullable = false)
    @Comment("이미지 원본명")
    private String ImageName;

    @Column(nullable = false)
    @Comment("이미지 uuid")
    private UUID uuid;

    @Column(name = "file_extension")
    @Comment("파일 확장자")
    private String fileExtension;

    @Transient
    public String getImageFullName() {
        return this.uuid.toString() + this.fileExtension;
    }
}
