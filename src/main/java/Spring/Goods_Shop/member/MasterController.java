package Spring.Goods_Shop.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterController {

    @GetMapping("/checkout/list")
    String masterCheckoutListPage(){
        return "master/checkout/list";
    }

    @GetMapping("/checkout/details")
    String masterCheckoutDetailsPage(){
        return "master/checkout/details";
    }
}
