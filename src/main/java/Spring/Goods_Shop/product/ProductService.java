package Spring.Goods_Shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public Product save(ProductRequestDto requestDto) {
        return productRepository.save(requestDto.toEntity());
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 ID"));
        productRepository.delete(product);
    }
}
