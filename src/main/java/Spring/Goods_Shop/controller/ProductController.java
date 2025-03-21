package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.product.*;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.service.MemberService;
import Spring.Goods_Shop.service.ProductImageService;
import Spring.Goods_Shop.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    private final ProductImageService productImageService;

    private final MemberService memberService;


    @GetMapping("/product/list")
    public String productList(
            @RequestParam(defaultValue = "0") int page, // 페이지 시작
            @RequestParam(defaultValue = "10") int size, // 상품 분류 기본 개수
            @RequestParam(defaultValue = "default") String sort, // 상품 정렬
            Model model, HttpServletRequest request) {
        // url에서 50 을 초과할시 25의 값을 줌
        if (size > 50) {
            size = 25;
        }
        Member member = memberService.getMemberEntity(request);
        if (member != null) {
            model.addAttribute("name", member.getName());
            model.addAttribute("userId", member.getUserId());
        }

        Page<ProductListResponseDto> productListResponseDtoPage = productService.getProductListResponseDto(page, size, sort);
        model.addAttribute("productList", productListResponseDtoPage.getContent());
        model.addAttribute("page", productListResponseDtoPage);
        model.addAttribute("currentPage", productListResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("check" ,"1");

        return "product/product-list";
    }

    @GetMapping("/product/details/{id}")
    public String productDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        ProductDetailsResponseDto productDetailsResponseDto = productService.toProductDetailsResponseDto(id);
        model.addAttribute("product", productDetailsResponseDto);

        Member member = memberService.getMemberEntity(request);
        if (member != null) {
            model.addAttribute("name", member.getName());
            model.addAttribute("userId", member.getUserId());
        }

        return "product/product-detail";
    }

    @GetMapping("/product/list/category")
    public String getProductCategory(@RequestParam(value = "category", required = false, defaultValue = "") String category,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size,
                                     @RequestParam(defaultValue = "default") String sort,
                                     Model model, HttpServletRequest request) {

        Member member = memberService.getMemberEntity(request);
        if (member != null) {
            model.addAttribute("name", member.getName());
            model.addAttribute("userId", member.getUserId());
        }

        Page<ProductCategoryAndSearchResponseDto> productCategoryResponseDtoPage = productService.getProductCategoryResponseListDto(category, page, size, sort);

        model.addAttribute("categoryList", productCategoryResponseDtoPage.getContent());
        model.addAttribute("page", productCategoryResponseDtoPage);
        model.addAttribute("currentPage", productCategoryResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("categoryQuery", category); // 현재 선택된 카테고리
        model.addAttribute("sortSelect", sort); // 현재 선택된 정렬 방식

        return "product/product-category";
    }

    @GetMapping("/product/search/index")
    public String getSearchProductName(@RequestParam(value = "keyword", required = false, defaultValue = "검색어를 입력해주세요.") String name,
                                       @RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "default") String sort,
                                       Model model, HttpServletRequest request) {

        Member member = memberService.getMemberEntity(request);
        if (member != null) {
            model.addAttribute("name", member.getName());
            model.addAttribute("userId", member.getUserId());
        }
        // 검색어에 공백을 지움
        String cleanName = name.replace(" ", "");
        Page<ProductCategoryAndSearchResponseDto> productCategoryAndSearchResponseDtoPage = productService.getProductNameResponseListDto(cleanName, page, size, sort);

        model.addAttribute("searchList", productCategoryAndSearchResponseDtoPage.getContent());
        model.addAttribute("page", productCategoryAndSearchResponseDtoPage);
        model.addAttribute("currentPage", productCategoryAndSearchResponseDtoPage.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("keywordQuery", name);
        model.addAttribute("sortSelect", sort);

        return "product/product-search";
    }

    // 등록된 상품 리스트
    @GetMapping("/master/product/list")
    public String masterProductList(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "default") String sort,
                                    Model model) {
        Page<MasterProductListResponseDto> productList = productService.getMasterProductListDto(page, size, sort);
        model.addAttribute("productList", productList.getContent());
        model.addAttribute("page", productList);
        model.addAttribute("currentPage", productList.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        return "product/master-product-list";
    }

    // 등록된 상품 리스트 검색
    @GetMapping("/master/product/search/index")
    public String masterProductListSearch(@RequestParam(value = "keyword", required = false, defaultValue = "검색어를 입력해주세요.") String name,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "default") String sort,
                                           Model model) {
        // 검색어에 공백을 지움
        String cleanName = name.replace(" ", "");
        Page<MasterProductListResponseDto> productList = productService.getMasterProductSearchListDto(page, size, sort, cleanName);
        model.addAttribute("productList", productList.getContent());
        model.addAttribute("page", productList);
        model.addAttribute("currentPage", productList.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("keywordQuery", name);
        return "product/master-product-search";
    }

    // 등록된 상품 리스트 카테고리
    @GetMapping("/master/product/list/category")
    public String masterProductListCategory(@RequestParam(value = "category", required = false, defaultValue = "") String category,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(defaultValue = "default") String sort,
                                          Model model) {
        Page<MasterProductListResponseDto> productList = productService.getMasterProductCategoryListDto(page, size, sort, category);
        model.addAttribute("productList", productList.getContent());
        model.addAttribute("page", productList);
        model.addAttribute("currentPage", productList.getNumber());
        model.addAttribute("size", size);
        model.addAttribute("sortSelect", sort);
        model.addAttribute("categoryQuery", category);
        return "product/master-product-category";
    }

    // 상품 등록 페이지 이동
    @GetMapping("/master/product/register")
    public String productRegister() {
        return "product/product-register";
    }

    // 상품 저장
    @PostMapping("/master/product/save")
    public String saveProduct(@Valid @ModelAttribute ProductRequestDto productRequestDto) {

        productService.save(productRequestDto);

        return "redirect:/master/product/list";
    }

    // 수정 요청
    @GetMapping("/master/product/edit/{id}")
    public String productEdit(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);
        ProductImageUrlDto productImageUrlDto = productImageService.getProductImageDto(id);
        model.addAttribute("product", product);
        model.addAttribute("subImage", productImageUrlDto.getSubImageUrl());
        model.addAttribute("descImage", productImageUrlDto.getDescImageUrl());
        return "product/product-edit";
    }

    // 수정 완료
    @PostMapping("/master/product/edit/{id}")
    public String productEditComplete(@PathVariable Long id,
                                      @Valid @ModelAttribute ProductRequestDto requestDto,
                                      @RequestParam(value = "deletedImages", required = false) String deletedImages) {
        productService.update(id, requestDto, deletedImages);
        return "redirect:/master/product/edit/" + id;
    }

    // 상품 삭제
    @GetMapping("/master/product/delete/{id}")
    public String productDelete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/master/product/list";
    }
}
