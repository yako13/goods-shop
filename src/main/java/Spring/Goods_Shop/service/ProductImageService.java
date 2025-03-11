package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.product.ProductImageUrlDto;
import Spring.Goods_Shop.entity.ProductImage;
import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.dto.product.ProductRequestDto;
import Spring.Goods_Shop.inter.ProductImageManager;
import Spring.Goods_Shop.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ImageRepository imageRepository;


    private final ProductImageManager productImageManager;

    // 단일 이미지 저장
    public ProductImage save(MultipartFile imageFile, Product product, ImageType imageType) {
        if (imageFile.isEmpty()) return null; // 빈 파일은 처리하지 않음

        // 이미지를 저장하고 UUID를 반환받음
        UUID imageUuid = productImageManager.save(imageFile);

        // ProductImage 객체 생성 후 저장
        ProductImage productImage = ProductImage.builder()
                .uuid(imageUuid)
                .fileExtension(productImageManager.getExtensionOf(imageFile))
                .imageType(imageType)
                .imageName(imageFile.getOriginalFilename())
                .product(product)
                .build();

        return imageRepository.save(productImage); // 저장된 이미지 객체 반환
    }

    // 여러 이미지를 저장하는 메서드
    private void multiSave(List<MultipartFile> imageFiles, Product product, ImageType imageType) {
        if (imageFiles == null || imageFiles.isEmpty()) return; // 빈 리스트는 처리하지 않음

        imageFiles.forEach(imageFile -> save(imageFile, product, imageType)); // 각 이미지를 저장
    }

    // 메인, 서브, 설명 이미지 처리
    public ProductImage create(ProductRequestDto requestDto, Product product) {
        // 서브 이미지와 설명 이미지 처리
        if (requestDto.getSubImage() != null && !requestDto.getSubImage().isEmpty()) {
            multiSave(requestDto.getSubImage(), product, ImageType.SUB); // 서브 이미지 저장
        }

        if (requestDto.getDescImage() != null && !requestDto.getDescImage().isEmpty()) {
            multiSave(requestDto.getDescImage(), product, ImageType.DESC); // 설명 이미지 저장
        }

        // 메인 이미지 처리
        if (requestDto.getMainImage() != null && !requestDto.getMainImage().isEmpty()) {
            return save(requestDto.getMainImage(), product, ImageType.MAIN); // 메인 이미지 저장 후 반환
        }

        return product.getProductImage(); // 메인 이미지가 없으면 기존 이미지 반환
    }


    // 이미지 URL을 반환하는 메서드
    public ProductImageUrlDto getProductImageDto(Long id) {
        List<ProductImage> productImageList = imageRepository.findByProductId(id);

        String productMainImageUrl = null;
        List<String> productSubImageUrl = new ArrayList<>();
        List<String> productDescImageUrl = new ArrayList<>();

        //이미지 타입에 따른 분류
        for (ProductImage productImage : productImageList) {
            String imageUrl = productImageManager.createImageUrl(productImage.getImageFullName());
            switch (productImage.getImageType()) {
                case MAIN -> productMainImageUrl = imageUrl;
                case SUB ->
                        productSubImageUrl.add(imageUrl);
                case DESC ->
                        productDescImageUrl.add(imageUrl);
            }
        }

        return ProductImageUrlDto.builder()
                .mainImageUrl(productMainImageUrl)
                .subImageUrl(productSubImageUrl)
                .descImageUrl(productDescImageUrl)
                .build();
    }

}