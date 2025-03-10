package Spring.Goods_Shop.productImage;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}
