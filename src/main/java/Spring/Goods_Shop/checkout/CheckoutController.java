package Spring.Goods_Shop.checkout;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/master/checkout/list")
    String masterCheckoutListPage(@PageableDefault(size = 10, page = 0) Pageable pageable, Model model) {
        Page<CheckoutResponseDto> responseDtos = checkoutService.getCheckoutList(pageable);
        model.addAttribute("checkoutList", responseDtos.getContent());
        model.addAttribute("paging", responseDtos);
        return "master/checkout/list";
    }

    @GetMapping("/master/checkout/details")
    String masterCheckoutDetailsPage() {
        return "master/checkout/details";
    }

    //    테스트 페이지 이동 컨트롤러
    @GetMapping("/test10")
    public String test10Go(HttpServletRequest request, Model model) {


        return "testPageUrl";
    }

    // 주문 / 결제 페이지로 이동
    @GetMapping("/checkout")
    public String checkoutGo(HttpServletRequest request, Model model) {


        return "pages/checkout";
    }

    //주문 완료 페이지로 이동
    @GetMapping("/checkout/complete")
    public String checkoutCompleteGo(HttpServletRequest request, Model model) {


        return "pages/checkoutComplete";
    }

    //주문 목록 페이지로 이동
    @GetMapping("/member/checkout/list")
    public String checkoutListGo(HttpServletRequest request, Model model) {


        return "pages/checkoutList";
    }

    //주문 목록 상세 페이지로 이동
    @GetMapping("/member/checkout/details/")
    public String checkoutDetailsGo(HttpServletRequest request, Model model) {

        return "pages/checkoutListDetail";
    }


}
