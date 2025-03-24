package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.checkout.HanPart.CheckoutDeliveryResponseDto;
import Spring.Goods_Shop.dto.checkout.HanPart.CheckoutPayResponseDto;
import Spring.Goods_Shop.service.HanCheckoutService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class HanCheckoutApiController {

    private final HanCheckoutService hanCheckoutService;

    //배송지 삭제 ajax
    @DeleteMapping("/checkout/cart/address/delete/{id}")
    public ResponseEntity<String> hanDeleteAddress(@PathVariable Long id, HttpSession session) {

        try {

            hanCheckoutService.deleteDelivery(id);

            //신규등록한 배송지를 삭제할경우 리다이렉트할때 다시 나타나지않게 해주는 함수 (/checkout/cart)
            if (session.getAttribute("deliveryNewCart") != null) {

                CheckoutDeliveryResponseDto checkoutCartPageDto = (CheckoutDeliveryResponseDto) session.getAttribute("deliveryNewCart");

                Long seesionId = checkoutCartPageDto.getId();

                //삭제요청한 배송지 pk와 세션에 담겨있는 배송지 pk가 같을경우 세션 삭제
                if (Objects.equals(id, seesionId)) {
                    session.removeAttribute("deliveryNewCart");
                }

            }

            //신규등록한 배송지를 삭제할경우 리다이렉트할때 다시 나타나지않게 해주는 함수 (/checkout)
            if (session.getAttribute("deliveryNew") != null) {

                CheckoutDeliveryResponseDto checkoutCartPageDto = (CheckoutDeliveryResponseDto) session.getAttribute("deliveryNew");

                Long seesionId = checkoutCartPageDto.getId();

                //삭제요청한 배송지 pk와 세션에 담겨있는 배송지 pk가 같을경우 세션 삭제
                if (Objects.equals(id, seesionId)) {
                    session.removeAttribute("deliveryNew");
                }

            }


            return ResponseEntity.ok("삭제 성공");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }

    //카드 삭제 ajax
    @DeleteMapping("/checkout/cart/pay/delete/{id}")
    public ResponseEntity<String> hanDeletePayCard(@PathVariable Long id , HttpSession session) {

        try {
            hanCheckoutService.deletePayCard(id);

            //신규등록한 배송지를 삭제할경우 리다이렉트할때 다시 나타나지않게 해주는 함수 (/checkout/cart)
            if (session.getAttribute("payNewCart") != null) {

                CheckoutPayResponseDto checkoutPayResponseDto = (CheckoutPayResponseDto) session.getAttribute("payNewCart");

                Long seesionId = checkoutPayResponseDto.getId();

                //삭제요청한 배송지 pk와 세션에 담겨있는 배송지 pk가 같을경우 세션 삭제
                if (Objects.equals(id, seesionId)) {
                    session.removeAttribute("payNewCart");
                }

            }

            //신규등록한 배송지를 삭제할경우 리다이렉트할때 다시 나타나지않게 해주는 함수 (/checkout)
            if (session.getAttribute("payNew") != null) {

                CheckoutPayResponseDto checkoutPayResponseDto = (CheckoutPayResponseDto) session.getAttribute("payNew");

                Long seesionId = checkoutPayResponseDto.getId();

                //삭제요청한 배송지 pk와 세션에 담겨있는 배송지 pk가 같을경우 세션 삭제
                if (Objects.equals(id, seesionId)) {
                    session.removeAttribute("payNew");
                }

            }



            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }


}
