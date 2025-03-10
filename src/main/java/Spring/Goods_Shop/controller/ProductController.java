package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.MasterProductListResponseDto;
import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.dto.product.ProductRequestDto;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.service.ProductImageService;
import Spring.Goods_Shop.dto.product.ProductImageUrlDto;
import Spring.Goods_Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    // 상품 등록 페이지 이동
    @GetMapping("/master/product/register")
    public String productRegister() {
        return "pages/product-register";
    }

    // 상품 저장
    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute ProductRequestDto productRequestDto){

        productService.save(productRequestDto);

        return "redirect:/master/product/list";
    }

    // 등록된 상품 리스트
    @GetMapping("/master/product/list")
    public String masterProductList(Product product, Model model) {
        List<MasterProductListResponseDto> productList = productService.getMasterProductListDto(product);
        model.addAttribute("productList", productList);
        return "pages/master-product-list";
    }

    // 수정 요청
    @GetMapping("/master/product/edit/{id}")
    public String productEdit(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);
        ProductImageUrlDto productImageUrlDto = productImageService.getProductImageDto(id);
        model.addAttribute("product", product);
        model.addAttribute("subImage", productImageUrlDto.getSubImageUrl());
        model.addAttribute("descImage", productImageUrlDto.getDescImageUrl());
        return "pages/product-edit";
    }

    // 수정 완료
    @PostMapping("/master/product/edit/{id}")
    public String productEditComplete(@PathVariable Long id, @ModelAttribute ProductRequestDto requestDto) {
            productService.update(id, requestDto);
        return "redirect:/master/product/edit/" + id;
    }

    // 상품 삭제
    @GetMapping("/master/product/delete/{id}")
    public String productDelete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/master/product/list";
    }

    @GetMapping("/product/list")
    public String productList(Product product, Model model) {
        List<ProductListResponseDto> productListResponseDtoList = productService.getProductListResponseDto(product);
        model.addAttribute("productList", productListResponseDtoList);
        return "pages/product-list";
    }

    @GetMapping("/product/detail")
    public String productDetail() {
        return "pages/product-detail";
    }
}
