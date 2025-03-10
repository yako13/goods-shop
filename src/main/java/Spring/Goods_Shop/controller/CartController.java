package Spring.Goods_Shop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    //장바구니 페이지로 이동
    @GetMapping("/cart")
    public String CartGo(HttpServletRequest request, Model model) {


        return "pages/cart";
    }




}
