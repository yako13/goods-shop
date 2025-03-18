package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.enums.MemberRole;
import Spring.Goods_Shop.service.MemberService;
import Spring.Goods_Shop.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final MemberService memberService;

    private final ProductService productService;

    @GetMapping("/")
    String createHomePage(Model model, HttpServletRequest request){

        List<ProductListResponseDto> productListResponseDtoList = productService.getSellingTop3Product();

        model.addAttribute("productList",productListResponseDtoList);


        Member member = memberService.getMemberEntity(request);

        if(member !=null && member.getRole().equals(MemberRole.CANCELLATION)){
            model.addAttribute("alert","탈퇴한 사용자입니다. 회원가입을 다시 진행해 주세요.");
            HttpSession session = request.getSession(false);
            session.invalidate();
            return "mainPage";
        }

        if(member!=null) {
            model.addAttribute("name", member.getName());
            model.addAttribute("userId", member.getUserId());

            return "mainPage";
        }
        return "mainPage";
    }
}
