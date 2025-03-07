package Spring.Goods_Shop.product;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.productImage.ImageRepository;
import Spring.Goods_Shop.productImage.ProductImage;
import Spring.Goods_Shop.productImage.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    private ProductMapper productMapper;

    @Transactional
    public void save(ProductRequestDto requestDto) {
        Product product = productRepository.save(requestDto.toEntity());

        ProductImage mainProductImage = productImageService.create(requestDto, product);

        product.setProductImage(mainProductImage);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 ID"));
        productRepository.delete(product);
    }
}
