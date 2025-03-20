package Spring.Goods_Shop.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TotalSalesResponseDto {

    private String day;

    private String totalSales;

}
