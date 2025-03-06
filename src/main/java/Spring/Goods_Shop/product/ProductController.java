package Spring.Goods_Shop.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @GetMapping("/master/product/register")
    public String productRegister() {
        return "pages/product-register";
    }

    // 상품 저장
    @PostMapping("/product/save")
    public String saveProduct(ProductRequestDto requestDto, Model model) {
        Product saveProduct = productService.save(requestDto);
        model.addAttribute("product", saveProduct);
        return "redirect:/master/product/list";
    }

    @GetMapping("/product/list")
    public String productList() {
        return "pages/product-list";
    }

    // 상품 삭제
    @GetMapping("/product/delete/{id}")
    public String productDelete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/master/product/list";
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "pages/product-detail";
    }
    @GetMapping("/master/product/edit")
    public String productEdit() {
        return "pages/product-edit";
    }


    @GetMapping("/master/product/list")
    public String masterProductList() {
        return "pages/master-product-list";
    }
}
