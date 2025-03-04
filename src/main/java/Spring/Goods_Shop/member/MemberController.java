package Spring.Goods_Shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/")
    String HomePage(){ return "mainPage";}

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

    @GetMapping("/member/edit")
    String memberEdit(){
        return "member/edit";
    }

    @GetMapping("/find/id")
    String findId(){
        return "member/find/id";
    }

    @GetMapping("/find/password")
    String findPassword(){
        return "member/find/password";
    }

    @GetMapping("/feedback/password")
    String feedbackPassword(){
        return "member/find/feedbackPassword";
    }

    @GetMapping("/feedback/id")
    String feedbackId(){
        return "member/find/feedbackId";
    }

    @GetMapping("/init/myPage")
    String initMyPage(){
        return "member/initMyPage";
    }
}
