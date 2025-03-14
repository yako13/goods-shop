package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.member.PayDto;
import Spring.Goods_Shop.dto.member.PayResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.ErrorService;
import Spring.Goods_Shop.service.MemberService;
import Spring.Goods_Shop.service.PayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    private final MemberService memberService;

    private final ErrorService errorService;

    @PostMapping("/check/defaultCard")
    @ResponseBody
    public String checkDefaultCard(PayDto payDto) {
        //기본결제카드가 존재할 때
        if (payService.checkDefaultCard(payDto)) {
            return "false";
        }
        //존재하지 않을 때
        return "true";
    }

    @PostMapping("/member/pay/create")
    private String createPayCard(@Valid PayDto payDto, Errors errors, Model model, HttpServletRequest request) {

        model.addAttribute("nickName", payDto.getNickName());

        model.addAttribute("cardNum1", payDto.getCardNum().get(0));
        model.addAttribute("cardNum2", payDto.getCardNum().get(1));
        model.addAttribute("cardNum3", payDto.getCardNum().get(2));
        model.addAttribute("cardNum4", payDto.getCardNum().get(3));

        model.addAttribute("expPeriod1", payDto.getExpPeriod().get(0));
        model.addAttribute("expPeriod2", payDto.getExpPeriod().get(1));

        model.addAttribute("cvc", payDto.getCvc());

        //정규식 검사
        if (errors.hasErrors()) {

            Map<String, String> validatorResult = errorService.validateHandling(errors);

            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }

            if (model.containsAttribute("valid_cardNum[0]") || model.containsAttribute("valid_cardNum[1]")
                    || model.containsAttribute("valid_cardNum[2]") || model.containsAttribute("valid_cardNum[3]")) {
                model.addAttribute("valid_cardNum", "카드번호는 숫자 네자리씩이어야 합니다.");
            }

            if (model.containsAttribute("valid_expPeriod[0]") || model.containsAttribute("valid_expPeriod[1]")) {
                model.addAttribute("valid_expPeriod", "카드 유효기간은 숫자 두자리씩이어야 합니다.");
            }

            return "/member/memberCardNew";

        }

        //기본 결제 카드가 있는데 또 추가하려고 시도할 경우
        if (payDto.isDefaultCard() && payService.checkDefaultCard(payDto)) {
            return "/member/memberCardNew";
        }

        payService.savePay(payDto, request);
        return "redirect:/member/pay/list";
    }

    //카드 목록 페이지로 이동
    @GetMapping("/member/pay/list")
    public String memberPayList(HttpServletRequest request, Model model) {

        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId",member.getUserId());
        model.addAttribute("name",member.getName());

        List<PayResponseDto> responseDtos = payService.getPayList(member);

        model.addAttribute("payList",responseDtos);

        return "member/memberCardList";
    }

    @GetMapping("/member/pay/delete/{id}")
    @ResponseBody
    public String deletePayCard(@PathVariable Long id){
        payService.deletePayCard(id);
        return "1000";
    }

    //카드 등록 페이지로 이동
    @GetMapping("/member/pay/create")
    public String memberPayCreatePage(HttpServletRequest request, RedirectAttributes rttr,Model model) {



          Member member = memberService.getMemberEntity(request);

          model.addAttribute("userId",member.getUserId());
          model.addAttribute("name",member.getName());


          //배송지 3개 이상 등록되어있을 경우 결제 등록 페이지로 못들어가게 막음
          if(!payService.checkCardCount(member)) {
              rttr.addFlashAttribute("alert","이미 결제수단이 3개 등록되어있습니다. 결제수단은 최대 3개까지 등록 가능합니다.");
              return "redirect:/member/pay/list";}


        return "member/memberCardNew";
    }

}
