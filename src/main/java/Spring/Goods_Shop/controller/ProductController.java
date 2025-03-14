package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.ProductCategoryAndSearchResponseDto;
import Spring.Goods_Shop.dto.product.ProductDetailsResponseDto;
import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping("/product/list")
    public String productList(
            @RequestParam(defaultValue = "0") int page, // 페이지 시작
            @RequestParam(defaultValue = "10") int size, // 상품 분류 기본 개수
            Model model) {
        Page<ProductListResponseDto> productListResponseDtoPage = productService.getProductListResponseDto(page, size);
        model.addAttribute("productList", productListResponseDtoPage.getContent());
        model.addAttribute("page", productListResponseDtoPage);
        model.addAttribute("currentPage", productListResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        return "product/product-list";
    }

    @GetMapping("/product/detail/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductDetailsResponseDto productDetailsResponseDto = productService.toProductDetailsResponseDto(id);
        model.addAttribute("product", productDetailsResponseDto);
        return "product/product-detail";
    }

    @GetMapping("/product/list/category")
    public String getProductCategory(@RequestParam(value = "category", required = false, defaultValue = "") String category,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size, Model model) {
        Page<ProductCategoryAndSearchResponseDto> productCategoryResponseDtoPage = productService.getProductCategoryResponseListDto(category, page, size);

        model.addAttribute("categoryList", productCategoryResponseDtoPage.getContent());
        model.addAttribute("page", productCategoryResponseDtoPage);
        model.addAttribute("currentPage", productCategoryResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("categoryQuery", category); // 현재 선택된 카테고리 저장

        return "product/product-category";
    }

    @GetMapping("/product/search/index")
    public String getSearchProductName(@RequestParam(value = "keyword", required = false, defaultValue = "")String name,
                                       @RequestParam(defaultValue = "0")int page,
                                       @RequestParam(defaultValue = "10")int size, Model model) {
        Page<ProductCategoryAndSearchResponseDto> productCategoryAndSearchResponseDtoPage = productService.getProductNameResponseListDto(name, page, size);

        model.addAttribute("searchList", productCategoryAndSearchResponseDtoPage.getContent());
        model.addAttribute("page", productCategoryAndSearchResponseDtoPage);
        model.addAttribute("currentPage", productCategoryAndSearchResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("keywordQuery", name);

        return "product/product-search";
    }
}
