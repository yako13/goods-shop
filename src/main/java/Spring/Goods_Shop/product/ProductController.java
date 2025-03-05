package Spring.Goods_Shop.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/product/list")
    public String productList() {
        return "pages/product-list";
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "pages/product-detail";
    }
    @GetMapping("/master/product/edit")
    public String productEdit() {
        return "pages/product-edit";
    }
    @GetMapping("/master/product/register")
    public String productRegister() {
        return "pages/product-register";
    }
    @GetMapping("/master/product/list")
    public String masterProductList() {
        return "pages/master-product-list";
    }
}
