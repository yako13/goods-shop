package Spring.Goods_Shop.productImage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ImageHandler {

    @Value("${config.product-image-location}")
    private String location;

    @Value("${config.product-image-resource-handler}")
    private String resourceHandler;

    public UUID save(MultipartFile imageFile) {
        try {
            UUID uuid = UUID.randomUUID();

            String originalName = imageFile.getOriginalFilename();

            if (originalName == null || originalName.isBlank()) throw new RuntimeException("이미지 파일의 이름이 존재하지 않음!");

            String fileExtension = originalName.substring(originalName.lastIndexOf("."));

            String uniqueFileName = uuid.toString() + fileExtension;

            Path filePath = Paths.get(location, uniqueFileName);
            Files.write(filePath, imageFile.getBytes());

            return uuid;
        } catch (IOException exception) {
            throw new RuntimeException("이미지 파일 저장 실패!", exception);
        }
    }

    public String createImageUrl(String imageFilename) {
        return resourceHandler + imageFilename;
    }

    public String getExtensionOf(MultipartFile imageFile) {
        String originalFileName = imageFile.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) throw new RuntimeException("유효한 이미지가 아닙니다!");
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }
}