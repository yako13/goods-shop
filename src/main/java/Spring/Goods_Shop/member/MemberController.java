package Spring.Goods_Shop.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    String createHomePage(){ return "mainPage";}

    @GetMapping("/login")
    String createLoginPage(){
        return "member/login";
    }

    @GetMapping("/join")
    String createJoinPage(){
        return "member/join";
    }

    @PostMapping("/join")
    String join(MemberJoinDto memberJoinDto, Model model){
        if(!memberService.join(memberJoinDto)) {
            model.addAttribute("duplicateId","아이디가 중복되었습니다.");
            return "member/join";}
        return "redirect:/login";
    }


    @GetMapping("/init/member")
    String createInitMyPage(HttpServletRequest request,Model model){

        MemberResponseDto memberResponseDto = memberService.checkOauthLogin(request);

        if(memberResponseDto==null) return "member/initMember";

        model.addAttribute("id",memberResponseDto.getMemberPK());
        model.addAttribute("userId",memberResponseDto.getUserId());
        model.addAttribute("name",memberResponseDto.getName());
        model.addAttribute("provider",memberResponseDto.getProvider());
        model.addAttribute("phoneNumber",memberResponseDto.getPhoneNumber());
        return "member/edit";

    }

    @PostMapping("/init/member")
    String initMyPage(MemberDto memberDto,HttpServletRequest request,Model model){

        MemberResponseDto memberResponseDto = memberService.initMyPage(memberDto,request);

        if(memberResponseDto==null) {
            model.addAttribute("error","비밀번호가 일치하지 않습니다.");
            return "member/initMember";
        }
        model.addAttribute("id",memberResponseDto.getMemberPK());
        model.addAttribute("userId",memberResponseDto.getUserId());
        model.addAttribute("userPassword",memberDto.getUserPassword());
        model.addAttribute("name",memberResponseDto.getName());
        model.addAttribute("provider",memberResponseDto.getProvider());
        model.addAttribute("phoneNumber",memberResponseDto.getPhoneNumber());
        return "member/edit";
    }

    @PostMapping("/member/edit")
    String memberEdit(MemberJoinDto memberJoinDto, HttpServletRequest request, Model model){
        memberService.tryMemberEdit(memberJoinDto);
        MemberResponseDto memberResponseDto = memberService.getMemberResponseDto(request);

        model.addAttribute("id",memberResponseDto.getMemberPK());
        model.addAttribute("userId",memberResponseDto.getUserId());
        model.addAttribute("userPassword",memberJoinDto.getUserPassword());
        model.addAttribute("name",memberResponseDto.getName());
        model.addAttribute("provider",memberResponseDto.getProvider());
        model.addAttribute("phoneNumber",memberResponseDto.getPhoneNumber());
        model.addAttribute("alert","수정이 완료되었습니다.");

        return "member/edit";
    }

    @GetMapping("/find/id")
    String createFindIdPage(){
        return "member/find/id";
    }

    @GetMapping("/find/password")
    String createFindPasswordPage(){
        return "member/find/password";
    }

    @GetMapping("/feedback/password")
    String createFeedbackPasswordPage(){
        return "member/find/feedbackPassword";
    }

    @GetMapping("/feedback/id")
    String createFeedbackIdPage(){
        return "member/find/feedbackId";
    }

}
