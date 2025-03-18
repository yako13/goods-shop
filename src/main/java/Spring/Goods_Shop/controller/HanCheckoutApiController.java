package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.service.HanCheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HanCheckoutApiController {

    private final HanCheckoutService hanCheckoutService;

    //배송지 삭제 ajax
    @DeleteMapping("/checkout/cart/address/delete/{id}")
    public ResponseEntity<String> hanDeleteAddress(@PathVariable Long id) {

        try {
            hanCheckoutService.deleteDelivery(id);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }

    //카드 삭제 ajax
    @DeleteMapping("/checkout/cart/pay/delete/{id}")
    public ResponseEntity<String> hanDeletePayCard(@PathVariable Long id) {

        try {
            hanCheckoutService.deletePayCard(id);
            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }








}
