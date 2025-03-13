package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.product.MasterProductListResponseDto;
import Spring.Goods_Shop.dto.product.ProductDetailsRequestDto;
import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.dto.product.ProductRequestDto;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.entity.ProductImage;
import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.inter.ProductImageManager;
import Spring.Goods_Shop.repository.ProductRepository;
import Spring.Goods_Shop.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductImageService productImageService;

    private final ProductImageManager productImageManager;

    // 상품 저장
    @Transactional
    public void save(ProductRequestDto requestDto) {
        Product product = productRepository.save(requestDto.toEntity());

        if (requestDto.getMainImage() != null & !requestDto.getMainImage().isEmpty()) {
            ProductImage mainProductImage = productImageService.create(requestDto, product);
            product.setProductImage(mainProductImage);
        }

        productRepository.save(product);
    }

    // pk 조회
    public Product getProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);

        if (optProduct.isEmpty()) throw new RuntimeException("해당 상품 없음");

        return optProduct.get();
    }

    // 등록된 상품 리스트 조회
    public List<MasterProductListResponseDto> getMasterProductListDto(Product product) {

        List<Product> productList = productRepository.findAll();

        return productList.stream().map(this::toMasterProductListResponseDto).toList();
    }

    public MasterProductListResponseDto toMasterProductListResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        return MasterProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().setScale(0, RoundingMode.FLOOR))
                .count(product.getCount())
                .mainImagePath(productMainImagePath)
                .productCategory(Formatter.getProductCategory(product.getProductCategory()))
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }

    // 등록된 상품 삭제
    @Transactional
    public void delete(Long id) {

        Product product = getProduct(id);

        productRepository.delete(product);
    }

    @Transactional
    public Product update(Long id, ProductRequestDto requestDto) {
        Product product = getProduct(id); // 상품 조회
        product.update(requestDto); // 기존 상품 정보 업데이트

        // 메인 이미지가 있으면 업데이트, 없으면 기존 이미지 유지
        if (requestDto.getMainImage() != null && !requestDto.getMainImage().isEmpty()) {
            ProductImage updatedImage = productImageService.create(requestDto, product);
            product.setProductImage(updatedImage); // 새로운 메인 이미지 설정
        }

        // 서브 이미지, 설명이미지 추가되면 업데이트
        if (requestDto.getSubImage() != null && !requestDto.getSubImage().isEmpty() ||
        requestDto.getDescImage() != null && !requestDto.getDescImage().isEmpty()) {
            productImageService.create(requestDto, product); // 서브 이미지 저장
        }

        return productRepository.save(product); // 상품 정보 저장
    }

    public List<ProductListResponseDto> getProductListResponseDto(Product product) {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::toProductListResponseDto).toList();
    }

    public ProductListResponseDto toProductListResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }
        return ProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice().setScale(0, RoundingMode.FLOOR))
                .mainImagePath(productMainImagePath)
                .build();
    }

    public ProductDetailsRequestDto toProductDetailsRequestDto(Long id) {
        Product product = getProduct(id);
        return ProductDetailsRequestDto.builder()
                .id(product.getId())
                .name(product.getName())
                .count(product.getCount())
                .price(product.getPrice())
                .productDescription(product.getProductDescription())
                .mainImage(product.getProductImage())
                .subImage(product.getProductImageList().stream()
                        .filter(productImage -> productImage.getImageType() == ImageType.SUB).toList())
                .descImage(product.getProductImageList().stream()
                        .filter(productImage -> productImage.getImageType() == ImageType.DESC).toList())
                .build();
    }

}
