package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.ProductDetailsRequestDto;
import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/product/list")
    public String productList(Product product, Model model) {
        List<ProductListResponseDto> productListResponseDtoList = productService.getProductListResponseDto(product);
        model.addAttribute("productList", productListResponseDtoList);
        return "product/product-list";
    }

    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductDetailsRequestDto productDetailsRequestDto = productService.toProductDetailsRequestDto(id);
        model.addAttribute("product", productDetailsRequestDto);
        return "product/product-detail";
    }
}
