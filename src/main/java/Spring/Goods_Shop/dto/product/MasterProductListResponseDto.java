package Spring.Goods_Shop.dto.product;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterProductListResponseDto {

    private Long id;
    private String name;
    private String price;
    private int count;
    private String mainImagePath;
    private String productCategory;
    private String createdAt;
    private String modifiedAt;
}
