package Spring.Goods_Shop.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCheckoutDto {

    private List<Long> cartIdList;                //카트 pk

    private List<Integer>  cartCnt;         //상품 개수

}
