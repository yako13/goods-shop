package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.MasterProductListResponseDto;
import Spring.Goods_Shop.dto.product.ProductImageUrlDto;
import Spring.Goods_Shop.dto.product.ProductRequestDto;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.service.ProductImageService;
import Spring.Goods_Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterController {

    private final ProductService productService;

    private final ProductImageService productImageService;


    // 등록된 상품 리스트
    @GetMapping("/product/list")
    public String masterProductList(Product product, Model model) {
        List<MasterProductListResponseDto> productList = productService.getMasterProductListDto(product);
        model.addAttribute("productList", productList);
        return "product/master-product-list";
    }

    // 상품 등록 페이지 이동
    @GetMapping("/product/register")
    public String productRegister() {
        return "product/product-register";
    }

    // 상품 저장
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute ProductRequestDto productRequestDto){

        productService.save(productRequestDto);

        return "redirect:/master/product/list";
    }

    // 수정 요청
    @GetMapping("/product/edit/{id}")
    public String productEdit(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);
        ProductImageUrlDto productImageUrlDto = productImageService.getProductImageDto(id);
        model.addAttribute("product", product);
        model.addAttribute("subImage", productImageUrlDto.getSubImageUrl());
        model.addAttribute("descImage", productImageUrlDto.getDescImageUrl());
        return "product/product-edit";
    }

    // 수정 완료
    @PostMapping("/product/edit/{id}")
    public String productEditComplete(@PathVariable Long id, @ModelAttribute ProductRequestDto requestDto) {
        productService.update(id, requestDto);
        return "redirect:/master/product/edit/" + id;
    }

    // 상품 삭제
    @GetMapping("/product/delete/{id}")
    public String productDelete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/master/product/list";
    }
}
