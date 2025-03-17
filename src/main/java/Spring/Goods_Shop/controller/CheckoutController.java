package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.checkout.CheckoutDetailsResponseDto;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsDto;
import Spring.Goods_Shop.dto.checkout.CheckoutResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.CheckoutService;
import Spring.Goods_Shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    private final MemberService memberService;

    @GetMapping("/master/checkout/list")
    public String masterCheckoutListPage(@PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<CheckoutResponseDto> responseDtos = checkoutService.getCheckoutList(pageable);
        model.addAttribute("checkoutList", responseDtos.getContent());
        model.addAttribute("paging", responseDtos);
        model.addAttribute("total", responseDtos.getTotalElements());
        model.addAttribute("currentPage", responseDtos.getNumber());
        return "checkout/masterList";
    }

    @GetMapping("/master/checkout/details/{id}")
    public String masterCheckoutDetailsPage(@PathVariable Long id, Model model) {
        CheckoutDetailsResponseDto responseDto = checkoutService.getCheckoutDetails(id);

        model.addAttribute("checkoutDetails", responseDto);
        model.addAttribute("productList", responseDto.getProductList());

        return "checkout/masterDetails";
    }

    @PostMapping("/master/checkout/edit")
    public String editCheckout(CheckoutDetailsDto checkoutDetailsDto, RedirectAttributes rttr) {
        checkoutService.editCheckout(checkoutDetailsDto);
        rttr.addFlashAttribute("alert", "수정이 완료되었습니다.");
        return "redirect:/master/checkout/details/" + checkoutDetailsDto.getId();
    }

    @GetMapping("/master/checkout/{id}/delete")
    public String deleteCheckout(@PathVariable Long id, RedirectAttributes rttr) {
        checkoutService.deleteCheckout(id);
        rttr.addFlashAttribute("alert", "삭제가 완료되었습니다.");
        return "redirect:/master/checkout/list";
    }

    @GetMapping("/member/checkout/list")
    public String createMemberCheckoutList(HttpServletRequest request, Model model) {
        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId", member.getUserId());
        model.addAttribute("name", member.getName());

        List<CheckoutResponseDto> checkoutResponseDtos = checkoutService.getMemberCheckoutList(member);

        model.addAttribute("checkoutList", checkoutResponseDtos);

        return "checkout/checkoutList";
    }


}
