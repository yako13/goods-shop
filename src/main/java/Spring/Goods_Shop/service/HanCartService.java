package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.product.Hanpart.ProductCheckoutResDto;
import Spring.Goods_Shop.entity.Cart;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.repository.HanCartRepository;
import Spring.Goods_Shop.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HanCartService {

    private final HanCartRepository hanCartRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    //장바구니 페이지로 이동
    public List<Cart> cartListGo(HttpServletRequest request) {

        //세션에서 맴버 정보 가져옴
        Member memberEntity = memberService.getMemberEntity(request);

        //장바구니 리스트 가져옴
        List<Cart> cartList = memberEntity.getCartList();

        return cartList;

    }


    //  선택한 상품 삭제

    public void removeSelectedItems(List<Long> itemIds) {
        hanCartRepository.deleteAllById(itemIds);
    }

    // 전체 삭제

    public void removeAllItems(HttpServletRequest request) {

        //세션에서 맴버 정보 가져옴
        Member memberEntity = memberService.getMemberEntity(request);

        hanCartRepository.deleteAllByMemberId(memberEntity.getId());
    }

    //  상품 개수 업데이트
    public String updateQuantity(Long cartId, int cartCnt) {
        Optional<Cart> cart = hanCartRepository.findById(cartId);
        if (cart.isPresent()) {
            Cart item = cart.get();
            item.setCartCnt(cartCnt);
            hanCartRepository.save(item);
            return "상품 수량이 업데이트되었습니다.";
        }
        return "상품을 찾을 수 없습니다.";
    }

    //상품 상세페이지에서 장바구니 추가
    public String cartAdd(HttpServletRequest request, ProductCheckoutResDto form) {

        //세션에서 맴버 정보 가져옴
        Member memberEntity = memberService.getMemberEntity(request);

        //form에 정보가 없을경우 실패
        if (form.getProductCnt() == 0 || form.getProductPk() == null) {

            return "데이터 전송에 오류가 발생했습니다";
        }


        //상품 pk로 상품 엔티티 찾아옴
        Product product = productRepository.findById(form.getProductPk()).orElse(null);

        if (product == null) {


            return "상품 정보가 없습니다";
        }

        //상품 재고보다 구매할려는 상품 개수가 클경우 실패
        int count = product.getCount() - form.getProductCnt();

        if (count < 0) {

            return "죄송합니다 상품 재고가 부족합니다";
        }


        Cart cart = Cart.builder()
                .member(memberEntity)
                .product(product)
                .cartCnt(form.getProductCnt())
                .build();

        Cart cartEntity = hanCartRepository.save(cart);

        //저장이 안됐을경우
        if (cartEntity == null) {

            return "장바구니 생성에 오류가 발생했습니다";
        }


        return "success";
    }


}
