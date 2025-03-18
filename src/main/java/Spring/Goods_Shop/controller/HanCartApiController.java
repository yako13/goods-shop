package Spring.Goods_Shop.controller;


import Spring.Goods_Shop.dto.cart.CartDto;
import Spring.Goods_Shop.service.HanCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HanCartApiController {

    private final HanCartService hanCartService;



    //장바구니 수량 변경
    @PostMapping("/cart/update/quantity")
    public ResponseEntity<String> updateQuantity(@RequestBody CartDto request) {
        String message = hanCartService.updateQuantity(request.getCartId(), request.getCartCnt());

        if (message.equals("상품 수량이 업데이트되었습니다.")) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.badRequest().body(message);
    }




}
