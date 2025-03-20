package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.checkout.CheckoutDetailsResponseDto;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsDto;
import Spring.Goods_Shop.dto.checkout.CheckoutResponseDto;
import Spring.Goods_Shop.dto.checkout.HanPart.CheckoutListDetailDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.CheckoutService;
import Spring.Goods_Shop.service.HanCheckoutService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    private final MemberService memberService;

    private final HanCheckoutService hanCheckoutService;

    @GetMapping("/master/checkout/list")
    public String masterCheckoutListPage(
            @RequestParam(defaultValue = "ordererName") String search, //검색 기준
            @RequestParam(defaultValue = "0") int page, // 페이지 시작
            @RequestParam(defaultValue = "10") int size, // 상품 분류 기본 개수
            @RequestParam(defaultValue = "default") String sort, // 상품 정렬
            @RequestParam(defaultValue = "ALL") String checkoutState, //주문 상태
 Model model) {
        Page<CheckoutResponseDto> responseDtos = checkoutService.getCheckoutList(page,size,sort,checkoutState);
        model.addAttribute("checkoutList", responseDtos.getContent());
        model.addAttribute("paging", responseDtos);
        model.addAttribute("total", responseDtos.getTotalElements());
        model.addAttribute("currentPage", responseDtos.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("searchSelect",search);
        model.addAttribute("checkoutState",checkoutState);

        return "checkout/masterList";
    }

    @GetMapping("/master/checkout/search/index")
    public String masterSearchCheckoutListPage(
            @RequestParam(value = "keyword", required = false, defaultValue = "검색어를 입력해주세요.") String keyword,
            @RequestParam(defaultValue = "ordererName") String search,
            @RequestParam(defaultValue = "0") int page, // 페이지 시작
            @RequestParam(defaultValue = "10") int size, // 상품 분류 기본 개수
            @RequestParam(defaultValue = "default") String sort, // 상품 정렬
            @RequestParam(defaultValue = "ALL") String checkoutState, //주문 상태
            Model model) {
        Page<CheckoutResponseDto> responseDtos = checkoutService.getCheckoutSearchList(search,keyword,page,size,sort,checkoutState);
        model.addAttribute("checkoutList", responseDtos.getContent());
        model.addAttribute("paging", responseDtos);
        model.addAttribute("total", responseDtos.getTotalElements());
        model.addAttribute("currentPage", responseDtos.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("keywordQuery", keyword);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("searchSelect",search);
        model.addAttribute("checkoutState",checkoutState);

        return "checkout/masterSearchList";
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


    @GetMapping("/member/checkout/list")
    public String createMemberCheckoutList(HttpServletRequest request, Model model) {
        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId", member.getUserId());
        model.addAttribute("name", member.getName());

        List<CheckoutResponseDto> checkoutResponseDtos = checkoutService.getMemberCheckoutList(member);

        model.addAttribute("checkoutList", checkoutResponseDtos);

        return "checkout/checkoutList";
    }

    //    주문 목록 상세 페이지로 이동
    @GetMapping("/member/checkout/details/{id}")
    public String checkoutDetailsGo1(HttpServletRequest request, Model model, @PathVariable("id") Long id) {

        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId", member.getUserId());
        model.addAttribute("name", member.getName());

        //주문 목록 상세페이지 정보를 가져오고 변환 해주는 서비스
        CheckoutListDetailDto checkoutListDetailDto = hanCheckoutService.hanCheckoutListDetail(request, id);

        model.addAttribute("CheckoutListDetailDto", checkoutListDetailDto);

        return "checkout/checkoutListDetail";
    }


}
