package Spring.Goods_Shop.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
    String join(MemberAuthDto memberAuthDto, Model model){
        memberService.join(memberAuthDto);
        return "redirect:/login";
    }

    @PostMapping("/check/id")
    @ResponseBody
    String checkId(MemberAuthDto memberAuthDto){

        return memberService.checkId(memberAuthDto);
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
    String memberEdit(MemberAuthDto memberAuthDto, HttpServletRequest request, Model model){
        memberService.tryToEditMember(memberAuthDto,request);
        MemberResponseDto memberResponseDto = memberService.getMemberResponseDto(request);

        model.addAttribute("id",memberResponseDto.getMemberPK());
        model.addAttribute("userId",memberResponseDto.getUserId());
        model.addAttribute("userPassword", memberAuthDto.getUserPassword());
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

    @PostMapping("/find/id")
    String findId(MemberDto memberDto,Model model){
       MemberResponseDto memberResponseDto = memberService.tryToFindId(memberDto);
       if(memberResponseDto==null) {
           model.addAttribute("alert","해당 계정을 찾을 수 없습니다.");
           return "member/find/id";
       }

       model.addAttribute("userId",memberResponseDto.getUserId());
       return "member/find/feedbackId";
    }

    @GetMapping("/find/password")
    String createFindPasswordPage(){
        return "member/find/password";
    }

    @PostMapping("/find/password")
    String findPassword(MemberDto memberDto,Model model){
       MemberResponseDto memberResponseDto = memberService.tryToFindPassword(memberDto);

       if(memberResponseDto == null){
           model.addAttribute("alert","해당 계정을 찾을 수 없습니다.");
           return "member/find/password";
       }

       model.addAttribute("userId",memberResponseDto.getUserId());
       return "member/find/feedbackPassword";
    }

    @PostMapping("/password/edit")
    String editPassword(MemberAuthDto memberAuthDto){
        memberService.tryToEditPassword(memberAuthDto);
        return "redirect:/login";
    }

    @GetMapping("/account/cancellation")
    String createAccountCancellationPage(HttpServletRequest request,RedirectAttributes rttr){
       Member member =  memberService.getMemberEntity(request);

       if(member.getProvider()==null){
           return "member/accountCancellation";
       }

        memberService.tryToCancellationSNSAccount(request);
        rttr.addFlashAttribute("alert","회원 탈퇴가 완료되었습니다.");
        return "redirect:/";
    }

    @PostMapping("/account/cancellation")
    String accountCancellation(MemberAuthDto memberAuthDto, HttpServletRequest request, RedirectAttributes rttr){
        if(!memberService.tryToCancellationAccount(memberAuthDto,request)){
            rttr.addFlashAttribute("alert","입력 정보가 잘못되었습니다.");
            return "redirect:/account/cancellation";
        }
        rttr.addFlashAttribute("alert","회원 탈퇴가 완료되었습니다.");
        return "redirect:/";
    }

}
