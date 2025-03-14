package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.*;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.service.ProductImageService;
import Spring.Goods_Shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    private final ProductImageService productImageService;



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

    // 등록된 상품 리스트
    @GetMapping("/product/list")
    public String masterProductList(Model model, @PageableDefault Pageable pageable) {
        Page<MasterProductListResponseDto> productList = productService.getMasterProductListDto(pageable);
        model.addAttribute("productList", productList.getContent());
        model.addAttribute("page", productList);
        model.addAttribute("currentPage", productList.getNumber());
        return "product/master-product-list";
    }

    // 상품 등록 페이지 이동
    @GetMapping("/product/register")
    public String productRegister() {
        return "product/product-register";
    }

    // 상품 저장
    @PostMapping("/product/save")
    public String saveProduct(@Valid @ModelAttribute ProductRequestDto productRequestDto){

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
    public String productEditComplete(@PathVariable Long id,
                                      @Valid @ModelAttribute ProductRequestDto requestDto,
                                      @RequestParam(value = "deletedImages", required = false) String deletedImages) {
        productService.update(id, requestDto, deletedImages);
        return "redirect:/master/product/edit/" + id;
    }

    // 상품 삭제
    @GetMapping("/product/delete/{id}")
    public String productDelete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/master/product/list";
    }
}
