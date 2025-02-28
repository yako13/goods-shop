package Spring.Goods_Shop.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    String loginPage(){
        return "member/login";
    }

    @GetMapping("/join")
    String joinPage(){
        return "member/join";
    }

    @PostMapping("/join")
    String join(MemberJoinDto memberJoinDto, Model model){
        return "redirect:/";
    }

    @GetMapping("/find/id")
    String findId(){
        return "member/find/id";
    }
}
