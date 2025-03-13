package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.enums.MemberRole;
import Spring.Goods_Shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final MemberService memberService;

    @GetMapping("/")
    String createHomePage(Model model, HttpServletRequest request){
        Member member = memberService.getMemberEntity(request);
        if(member==null){
            return "mainPage";
        }

        //관리자로 로그인 했을 때
        if(member.getRole().equals(MemberRole.ADMIN)){
            return "redirect:/master/checkout/list";
        }

        model.addAttribute("name",member.getName());
        model.addAttribute("userId",member.getUserId());

        return "mainPage";
    }
}
