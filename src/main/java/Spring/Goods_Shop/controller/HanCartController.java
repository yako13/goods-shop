package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.Hanpart.ProductCheckoutResDto;
import Spring.Goods_Shop.entity.Cart;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.HanCartService;
import Spring.Goods_Shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class HanCartController {

    private final HanCartService hanCartService;

    private final MemberService memberService;

    //장바구니 페이지로 이동
    @GetMapping("/cart")
    public String CartGo(HttpServletRequest request, Model model) {

        Member member = memberService.getMemberEntity(request);
        if (member != null) {
            model.addAttribute("name", member.getName());
            model.addAttribute("userId", member.getUserId());
        }

        List<Cart> cartList = hanCartService.cartListGo(request);

        if (cartList.isEmpty()) {

            model.addAttribute("cartNo", 1);
        }

        model.addAttribute("cartList", cartList);

        return "cart";
    }

    //상품 상세 페이지에서 장바구니에 물건 등록
    @PostMapping("/cart/submit")
    public String CartSubmit(HttpServletRequest request, ProductCheckoutResDto form, RedirectAttributes rttr) {

        String info = hanCartService.cartAdd(request, form);

        //success 아닐경우
        if (!info.equals("success")) {

            //알람으로 상태를 알려줌
            rttr.addFlashAttribute("data", info);
        }

        return "redirect:/cart";
    }


    // 선택 삭제 (Form 방식)
    @PostMapping("/cart/remove")
    public String removeSelectedItems(@RequestParam("selectedItems") String selectedItems) {
        List<Long> itemIds = new ArrayList<>();

        String[] itemsArray = selectedItems.split(",");

        for (String item : itemsArray) {
            try {
                itemIds.add(Long.parseLong(item));  // 문자열을 Long으로 변환하여 리스트에 추가
            } catch (NumberFormatException e) {
                System.err.println(" 변환 오류: " + item); // 변환 실패 시 오류 메시지 출력
            }
        }
        hanCartService.removeSelectedItems(itemIds);

        return "redirect:/cart";  //  삭제 후 장바구니 페이지로 리다이렉트
    }

    // 전체 삭제 (ajax 방식)
    @PostMapping("/cart/clear")
    public ResponseEntity<String> removeAllItems(HttpServletRequest request) {
        try {
            hanCartService.removeAllItems(request);
            return ResponseEntity.ok("장바구니가 비워졌습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("장바구니 삭제 실패");
        }
    }



}
