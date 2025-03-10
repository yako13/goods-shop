package Spring.Goods_Shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HanMemberController {

    //카드 목록 페이지로 이동
    @GetMapping("/member/pay/list")
    public String memberPayListGo(HttpServletRequest request, Model model) {


        return "member/memberCardList";
    }

    //카드 등록 페이지로 이동
    @GetMapping("/member/pay/create")
    public String memberPayCreateGo(HttpServletRequest request, Model model) {


        return "member/memberCardNew";
    }

    //배송지 목록 페이지로 이동
    @GetMapping("/member/delivery/list")
    public String memberDeliveryListGo(HttpServletRequest request, Model model) {


        return "member/memberDeliveryList";
    }

    //배송지 등록 페이지로 이동
    @GetMapping("/member/delivery/create")
    public String memberDeliveryCreateGo(HttpServletRequest request, Model model) {


        return "member/memberDeliveryNew";
    }



}
