package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.member.MemberAuthDto;
import Spring.Goods_Shop.dto.member.MemberDto;
import Spring.Goods_Shop.dto.member.MemberEditDto;
import Spring.Goods_Shop.dto.member.MemberResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.ErrorService;
import Spring.Goods_Shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final ErrorService errorService;

    //로그인 화면
    @GetMapping("/login")
    String createLoginPage(){
        return "member/login";
    }

    @GetMapping("/master/login")
    String createMasterLoginPage(){
        return "member/masterLogin";
    }

    //회원가입 화면
    @GetMapping("/join")
    String createJoinPage(){
        return "member/join";
    }

    //회원 가입
    @PostMapping("/join")
    String join(@Valid MemberAuthDto memberAuthDto, Errors errors, Model model) throws IOException {

        if (errors.hasErrors()) {

            model.addAttribute("userId",memberAuthDto.getUserId());
            model.addAttribute("name",memberAuthDto.getName());
            model.addAttribute("phoneNumber",memberAuthDto.getPhoneNumber());

            Map<String, String> validatorResult = errorService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            return "member/join";
        }


        memberService.join(memberAuthDto);
        return "redirect:/login";
    }

    //아이디 중복 체크
    @PostMapping("/check/id")
    @ResponseBody
    String checkId(MemberAuthDto memberAuthDto){

        return memberService.checkId(memberAuthDto);
    }

    //마이페이지 접속
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

    //마이페이지 접속 시 비밀번호 일치 여부 확인
    @PostMapping("/init/member")
    String initMyPage(MemberDto memberDto, HttpServletRequest request, Model model){

        MemberResponseDto memberResponseDto = memberService.initMyPage(memberDto,request);

        if(memberResponseDto==null) {
            model.addAttribute("alert","비밀번호가 일치하지 않습니다.");
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

    //회원 수정
    @PostMapping("/member/edit")
    String memberEdit(@Valid MemberEditDto memberEditDto, Errors errors, HttpServletRequest request, Model model){

        if (errors.hasErrors()) {

            model.addAttribute("userId",memberEditDto.getUserId());
            model.addAttribute("name",memberEditDto.getName());
            model.addAttribute("provider",memberEditDto.getProvider());
            model.addAttribute("phoneNumber",memberEditDto.getPhoneNumber());
            model.addAttribute("userPassword",memberEditDto.getUserPassword());

            Map<String, String> validatorResult = errorService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "member/edit";
        }

        memberService.tryToEditMember(memberEditDto,request);
        MemberResponseDto memberResponseDto = memberService.getMemberResponseDto(request);

        model.addAttribute("id",memberResponseDto.getMemberPK());
        model.addAttribute("userId",memberResponseDto.getUserId());
        model.addAttribute("userPassword", memberEditDto.getUserPassword());
        model.addAttribute("name",memberResponseDto.getName());
        model.addAttribute("provider",memberResponseDto.getProvider());
        model.addAttribute("phoneNumber",memberResponseDto.getPhoneNumber());
        model.addAttribute("alert","수정이 완료되었습니다.");

        return "member/edit";
    }

    //아이디찾기
    @GetMapping("/find/id")
    String createFindIdPage(){
        return "member/findId";
    }

    //아이디 찾기
    @PostMapping("/find/id")
    @ResponseBody
    String findId(MemberDto memberDto){
        return memberService.tryToFindId(memberDto);
    }

    //비밀번호 찾기
    @GetMapping("/find/password")
    String createFindPasswordPage(){
        return "member/findPassword";
    }

    //비밀번호 찾기
    @PostMapping("/find/password")
    @ResponseBody
    String findPassword(MemberDto memberDto,Model model){
       return memberService.tryToFindPassword(memberDto);
    }

    //비밀번호 수정
    @PostMapping("/password/edit")
    String editPassword(MemberAuthDto memberAuthDto){
        memberService.tryToEditPassword(memberAuthDto);
        return "redirect:/login";
    }

    //계정 탈퇴
    @GetMapping("/account/cancellation")
    String createAccountCancellationPage(HttpServletRequest request,RedirectAttributes rttr,Model model){
       Member member =  memberService.getMemberEntity(request);
       model.addAttribute("name",member.getName());

       if(member.getProvider()==null){
           return "member/accountCancellation";
       }

        memberService.tryToCancellationSNSAccount(request);
        rttr.addFlashAttribute("alert","회원 탈퇴가 완료되었습니다.");
        return "redirect:/";
    }

    //계정 탈퇴
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
