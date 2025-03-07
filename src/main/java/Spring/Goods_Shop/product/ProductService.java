package Spring.Goods_Shop.product;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.productImage.ImageRepository;
import Spring.Goods_Shop.productImage.ProductImage;
import Spring.Goods_Shop.productImage.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    private final ProductMapper productMapper;

    @Transactional
    public void save(ProductRequestDto requestDto) {
        Product product = productRepository.save(requestDto.toEntity());

        ProductImage mainProductImage = productImageService.create(requestDto, product);

        product.setProductImage(mainProductImage);
    }

    public Product getProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);

        if (optProduct.isEmpty()) throw new RuntimeException("해당 상품 없음");

        return optProduct.get();
    }

    public List<MasterProductListResponseDto> getMasterProductListDto(Product product) {

        List<Product> productList = productRepository.findAll();

        return productList.stream().map(productMapper::toMasterProductListResponseDto).toList();
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 ID"));
        productRepository.delete(product);
    }
}
