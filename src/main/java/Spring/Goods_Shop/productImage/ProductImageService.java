package Spring.Goods_Shop.productImage;

import Spring.Goods_Shop.enums.ImageType;
import Spring.Goods_Shop.product.Product;
import Spring.Goods_Shop.product.ProductRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ImageRepository imageRepository;

    private final ProductImageMapper productImageMapper;

    private final ProductImageManager productImageManager;

    public ProductImage save(MultipartFile imageFile, Product product, ImageType imageType) {
        if (imageFile.isEmpty()) return null;

        UUID presentImageUuid = productImageManager.save(imageFile);

        ProductImage mainProductImage = ProductImage.builder()
                .uuid(presentImageUuid)
                .fileExtension(productImageManager.getExtensionOf(imageFile))
                .imageType(imageType)
                .imageName(imageFile.getOriginalFilename())
                .product(product)
                .build();

        return imageRepository.save(mainProductImage);
    }

    private void save(List<MultipartFile> imageFiles, Product product, ImageType imageType) {
        imageFiles.forEach((imageFile) -> {
            save(imageFile, product, imageType);
        });
    }

    public ProductImage create(ProductRequestDto requestDto, Product product) {
        save(requestDto.getSubImage(), product, ImageType.SUB);
        save(requestDto.getDescImage(), product, ImageType.DESC);
        return save(requestDto.getMainImage(), product, ImageType.MAIN);
    }
}