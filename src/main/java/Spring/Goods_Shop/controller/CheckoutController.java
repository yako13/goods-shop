package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.checkout.CheckoutDetailsResponseDto;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsDto;
import Spring.Goods_Shop.dto.checkout.CheckoutResponseDto;
import Spring.Goods_Shop.service.CheckoutService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @GetMapping("/master/checkout/list")
    public String masterCheckoutListPage(@PageableDefault(size = 10, page = 0) Pageable pageable, Model model) {
        Page<CheckoutResponseDto> responseDtos = checkoutService.getCheckoutList(pageable);
        model.addAttribute("checkoutList", responseDtos.getContent());
        model.addAttribute("paging", responseDtos);
        return "checkout/masterList";
    }

    @GetMapping("/master/checkout/details/{id}")
    public String masterCheckoutDetailsPage(@PathVariable Long id, Model model) {
        CheckoutDetailsResponseDto responseDto = checkoutService.getCheckoutDetails(id);

        model.addAttribute("checkoutDetails",responseDto);
        model.addAttribute("productList",responseDto.getProductList());

        return "checkout/masterDetails";
    }

    @PostMapping("/master/checkout/edit")
    public String editCheckout(CheckoutDetailsDto checkoutDetailsDto, RedirectAttributes rttr){
       checkoutService.editCheckout(checkoutDetailsDto);
       rttr.addFlashAttribute("alert","수정이 완료되었습니다.");
       return "redirect:/master/checkout/details/"+checkoutDetailsDto.getId();
    }

    @GetMapping("/master/checkout/details/{id}/delete")
    public String deleteCheckout(@PathVariable Long id, RedirectAttributes rttr){
        checkoutService.deleteCheckout(id);
        rttr.addFlashAttribute("alert","삭제가 완료되었습니다.");
        return "redirect:/master/checkout/list";
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
