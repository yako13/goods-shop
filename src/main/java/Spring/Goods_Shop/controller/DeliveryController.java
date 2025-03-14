package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.member.DeliveryDto;
import Spring.Goods_Shop.dto.member.DeliveryResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.DeliveryService;
import Spring.Goods_Shop.service.ErrorService;
import Spring.Goods_Shop.service.MemberService;
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
public class DeliveryController {

    private final ErrorService errorService;

    private final MemberService memberService;

    private final DeliveryService deliveryService;

    @PostMapping("/check/defaultDelivery")
    @ResponseBody
    public String checkDefaultDelivery(DeliveryDto deliveryDto) {
        //기본배송지가 존재할 때
        if (deliveryService.checkDefaultDelivery(deliveryDto)) {
            return "false";
        }
        //존재하지 않을 때
        return "true";
    }

    //배송지 목록 페이지로 이동
    @GetMapping("/member/delivery/list")
    public String memberDeliveryListGo(HttpServletRequest request, Model model) {
        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId",member.getUserId());
        model.addAttribute("name",member.getName());

        List<DeliveryResponseDto> deliveryResponseDtos = deliveryService.getDeliveryList(member);

        model.addAttribute("deliveryList",deliveryResponseDtos);

        return "member/memberDeliveryList";
    }

    //배송지 등록 페이지로 이동
    @GetMapping("/member/delivery/create")
    public String memberDeliveryCreatePage(HttpServletRequest request, RedirectAttributes rttr,Model model) {

        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId",member.getUserId());
        model.addAttribute("name",member.getName());

        if(!deliveryService.checkDeliveryCount(member)) {
            rttr.addFlashAttribute("alert","이미 배송지가 3개 등록되어있습니다. 배송지는 최대 3개까지 등록 가능합니다.");
            return "redirect:/member/delivery/list";
        }

        return "member/memberDeliveryNew";
    }

    @PostMapping("/member/delivery/create")
    public String memberDeliveryCreatePage(@Valid DeliveryDto deliveryDto, Errors errors,Model model,HttpServletRequest request){
        model.addAttribute("deliveryName",deliveryDto.getDeliveryName());
        model.addAttribute("recipientName",deliveryDto.getRecipientName());
        model.addAttribute("recipientPhoneNumber",deliveryDto.getRecipientPhoneNumber());
        model.addAttribute("postCode",deliveryDto.getPostCode());
        model.addAttribute("address",deliveryDto.getAddress());
        model.addAttribute("detailAddress",deliveryDto.getDetailAddress());
        model.addAttribute("memo",deliveryDto.getMemo());

        if(errors.hasErrors()){
            Map<String ,String>  validatorResult = errorService.validateHandling(errors);
            for(String key : validatorResult.keySet()){
                model.addAttribute(key,validatorResult.get(key));
            }

            return "member/memberDeliveryNew";

        }
        //기본 배송지가 있는데 또 추가하려고 시도할 경우
        if (deliveryDto.isDefaultDelivery() && deliveryService.checkDefaultDelivery(deliveryDto)) {
            return "/member/memberCardNew";
        }

        deliveryService.saveDelivery(deliveryDto,request);
        return "redirect:/member/delivery/list";
    }

    @GetMapping("/member/delivery/delete/{id}")
    @ResponseBody
    private String deleteDelivery(@PathVariable Long id){
        deliveryService.deleteDelivery(id);

        return "1000";
    }

}
