package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.member.PayDto;
import Spring.Goods_Shop.service.ErrorService;
import Spring.Goods_Shop.service.PayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

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
        return "/member/memberCardList";
    }

}
