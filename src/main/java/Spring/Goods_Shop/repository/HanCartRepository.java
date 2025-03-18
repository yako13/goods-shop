package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HanCartRepository extends JpaRepository<Cart, Long> {

    void deleteById(Long id); // 단일 아이템 삭제

    void deleteAllByMemberId(Long memberId); // 전체 삭제

    void deleteAllByIdIn(List<Long> ids); // 선택된 상품 삭제

    List<Cart> findByProductIdIn(List<Long> productIds); // 특정 상품 찾기

    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.id IN :cartIds")
    void checkoutSubmitCartDelete(@Param("cartIds") List<Long> cartIds);


}
