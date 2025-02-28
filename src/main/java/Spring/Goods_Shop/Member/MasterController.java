package Spring.Goods_Shop.Member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MasterController {

    @GetMapping("/master/checkout/list")
    String masterCheckoutListPage(){
        return "master/checkout";
    }
}
