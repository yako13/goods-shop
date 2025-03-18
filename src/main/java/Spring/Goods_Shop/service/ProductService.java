package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.product.*;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.entity.ProductImage;
import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.enums.ProductCategory;
import Spring.Goods_Shop.inter.ProductImageManager;
import Spring.Goods_Shop.repository.ImageRepository;
import Spring.Goods_Shop.repository.ProductRepository;
import Spring.Goods_Shop.util.FileStorageService;
import Spring.Goods_Shop.util.Formatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ImageRepository imageRepository;

    private final ProductImageService productImageService;

    private final ProductImageManager productImageManager;

    private final FileStorageService fileStorageService;

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

    // 등록된 상품 리스트 조회 호출 (관리자)
    public Page<MasterProductListResponseDto> getMasterProductListDto (int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt")); // 등록최신순 정렬
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(this::toMasterProductListResponseDto);
    }

    // 상품 리스트 조회 (관리자)
    public MasterProductListResponseDto toMasterProductListResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        return MasterProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(Formatter.changeBigDecimalFormat(product.getPrice()))
                .count(product.getCount())
                .mainImagePath(productMainImagePath)
                .productCategory(Formatter.getProductCategory(product.getProductCategory()))
                .createdAt(Formatter.getLocalDate(product.getCreatedAt()))
                .modifiedAt(Formatter.getLocalDate(product.getModifiedAt()))
                .build();
    }

    // 등록된 상품 삭제
    @Transactional
    public void delete(Long id) {

        Product product = getProduct(id);

        productRepository.delete(product);
    }

    // 상품 업데이트
    @Transactional
    public Product update(Long id, ProductRequestDto requestDto, String deletedImages) {
        Product product = getProduct(id);
        product.update(requestDto); // 기존 상품 정보 업데이트

        // SUB 이미지만 필터링
        List<ProductImage> subImageList = product.getProductImageList().stream()
                .filter(image -> image.getImageType() == ImageType.SUB)
                .collect(Collectors.toList());

        List<ProductImage> descImageList = product.getProductImageList().stream()
                .filter(image -> image.getImageType() == ImageType.DESC)
                .collect(Collectors.toList());

        // 삭제 요청이 있으면 이미지 삭제 처리
        if (deletedImages != null && !deletedImages.trim().isEmpty()) {
            String[] deletedImageList = deletedImages.split(",");

            for (String deletedImage : deletedImageList) {
                if (deletedImage.isEmpty()) continue;

                if (deletedImage.startsWith("subImage")) {
                    try {
                        int index = Integer.parseInt(deletedImage.replace("subImage", ""));
                        if (index < subImageList.size()) {
                            ProductImage imageToDelete = subImageList.get(index);
                            fileStorageService.deleteFile(imageToDelete.getImageFullName());

                            imageRepository.delete(imageToDelete); // DB에서 삭제
                            product.getProductImageList().remove(imageToDelete); // 리스트에서 제거
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid subImage index: " + deletedImage);
                    }
                }
                if (deletedImage.startsWith("descImage")) {
                    try {
                        int index = Integer.parseInt(deletedImage.replace("descImage", ""));
                        if (index < descImageList.size()) {
                            ProductImage imageToDelete = descImageList.get(index);
                            fileStorageService.deleteFile(imageToDelete.getImageFullName());

                            imageRepository.delete(imageToDelete); // DB에서 삭제
                            product.getProductImageList().remove(imageToDelete); // 리스트에서 제거
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid descImage index: " + deletedImage);
                    }
                }
            }
        }

        // 기존 이미지 삭제 후 새로운 이미지 저장
        ProductImage updatedImage = productImageService.create(requestDto, product);

        if (updatedImage != null) {
            updatedImage = imageRepository.save(updatedImage); // 먼저 저장
            product.setProductImage(updatedImage); // 저장된 이미지 연결
        }

        return productRepository.save(product);
    }

    // 상품 리스트 조회 호출 (멤버)
    public Page<ProductListResponseDto> getProductListResponseDto(int page, int size, String sort) {
        Pageable pageable;
        if ("price_asc".equals(sort)){
            pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "price"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "price"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        Page<Product> productList = productRepository.findAll(pageable);
        return productList.map(this::toProductListResponseDto);
    }

    // 상품 리스트 조회
    public ProductListResponseDto toProductListResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();

        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }
        return ProductListResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(Formatter.changeBigDecimalFormat(product.getPrice()))
                .mainImagePath(productMainImagePath)
                .build();
    }

    // 상품 상세 리스트
    public ProductDetailsResponseDto toProductDetailsResponseDto(Long id) {
        Product product = getProduct(id);
        return ProductDetailsResponseDto.builder()
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

    public Page<ProductCategoryAndSearchResponseDto> getProductCategoryResponseListDto(String category, int page, int size, String sort) {

        Pageable pageable;

        if ("price_asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "price"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "price"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        }

        ProductCategory productCategory = convertToProductCategory(category);

        Page<Product> products;
        if (productCategory == null) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findByProductCategory(productCategory, pageable);
        }

        return products.map(this::toProductCategoryResponseDto);
    }

    public ProductCategoryAndSearchResponseDto toProductCategoryResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();
        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        return ProductCategoryAndSearchResponseDto.builder()
                .id(product.getId())
                .productCategory(product.getProductCategory().name())
                .name(product.getName())
                .price(Formatter.changeBigDecimalFormat(product.getPrice()))
                .mainImagePath(productMainImagePath)
                .build();
    }

    public Page<ProductCategoryAndSearchResponseDto> getProductNameResponseListDto(String name, int page, int size, String sort) {

        Pageable pageable;

        if ("price_asc".equals(sort)) {
        pageable = PageRequest.of(page, size, Sort.by(Direction.ASC, "price"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "price"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));

        }

        if (name == null || name.isBlank()) return Page.empty();

        Page<Product> productPage = productRepository.findByNameContaining(name, pageable);

        return productPage.map(this::toProductNameResponseDto);
    }

    public ProductCategoryAndSearchResponseDto toProductNameResponseDto(Product product) {
        ProductImage productMainImage = product.getProductImage();
        String productMainImagePath = null;

        if (productMainImage != null) {
            productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
        }

        return ProductCategoryAndSearchResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(Formatter.changeBigDecimalFormat(product.getPrice()))
                .mainImagePath(productMainImagePath)
                .build();
    }

    //판매 개수가 제일 높은 3개의 항목 가져옴
    public List<ProductListResponseDto> getSellingTop3Product(){
         List<Product> productList =productRepository.findTop3ByOrderBySellingCountDescIdDesc();

         List<ProductListResponseDto> productListResponseDtoList = new ArrayList<>();

         for(Product product : productList){
             ProductImage productMainImage = product.getProductImage();
             String productMainImagePath = null;

             if (productMainImage != null) {
                 productMainImagePath = productImageManager.createImageUrl(productMainImage.getImageFullName());
             }

             ProductListResponseDto productListResponseDto = ProductListResponseDto.builder()
                     .id(product.getId())
                     .name(product.getName())
                     .price(Formatter.changeBigDecimalFormat(product.getPrice()))
                     .mainImagePath(productMainImagePath)
                     .build();

             productListResponseDtoList.add(productListResponseDto);
         }

        return productListResponseDtoList;
    }

    /**
     * 문자열을 ProductCategory Enum으로 변환하는 메서드
     */
    private ProductCategory convertToProductCategory(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }
        try {
            return ProductCategory.valueOf(category.toUpperCase().replace("-", "_")); // Enum과 매칭
        } catch (IllegalArgumentException e) {
            return null; // 존재하지 않는 카테고리 값이면 null 반환
        }
    }

}
