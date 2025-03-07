package Spring.Goods_Shop.productImage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Component
public class ProductImageManager {

    @Value("${config.product-image-location}")
    private String location;

    @Value("${config.product-image-resource-handler}")
    private String resourceHandler;

    // 허용하는 이미지 파일 확장자
    public UUID save(MultipartFile imageFile) {
        try {
            if (imageFile.isEmpty()) {
                throw new RuntimeException("업로드된 이미지 파일이 비어 있습니다.");
            }

            UUID uuid = UUID.randomUUID();
            String originalFilename = imageFile.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                throw new RuntimeException("이미지 파일의 이름이 존재하지 않음");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = uuid.toString() + fileExtension;

            // 지정된 경로에 파일 저장
            Path filePath = Paths.get(location, uniqueFilename);

            // 폴더 생성 확인
            File directory = new File(location);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    throw new RuntimeException("이미지 저장 폴더 생성 실패: " + location);
                }
            }

            // 파일 저장
            Files.write(filePath, imageFile.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("파일 저장 성공: " + filePath);

            return uuid;

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("상품 이미지 파일 저장 실패", ex);
        }
    }



    public String createImageUrl(String imageFileName) {
        return resourceHandler + imageFileName;
    }


    public String getExtensionOf(MultipartFile imageFile) {
        String originalFilename = imageFile.getOriginalFilename();

        if(originalFilename == null || originalFilename.isBlank()) throw new RuntimeException("유효하지 않은 이미지 파일명");

        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }
}