package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.enums.CheckoutState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
    Page<Checkout> findAll(Pageable pageable);

    List<Checkout> findAllByMemberId(Long memberId);

    //공백 구분 없이 주문자 이름 검색
    @Query("SELECT c FROM Checkout c WHERE LOWER(REPLACE(c.member.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%'))")
    Page<Checkout> findByMemberNameContainingWithoutSpace(@Param("withoutSpaceSearchText") String name, Pageable pageable);

    //공백 구분 없이 받는 분 이름 검색
    @Query("SELECT c FROM Checkout c WHERE LOWER(REPLACE(c.checkoutName, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%'))")
    Page<Checkout> findByCheckoutNameContainingWithoutSpace(@Param("withoutSpaceSearchText") String name, Pageable pageable);

    //공백 구분 없이 상품명 검색
    @Query("SELECT DISTINCT c FROM Checkout c " +
            "JOIN c.checkoutDetailsList d " +
            "JOIN d.product p " +
            "WHERE LOWER(REPLACE(p.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%'))")
    Page<Checkout> findByProductNameContainingWithoutSpace(
            @Param("withoutSpaceSearchText") String productName,
            Pageable pageable
    );

    //공백 구분 없이 주문자 이름 검색 + 주문상태
    @Query("SELECT c FROM Checkout c WHERE LOWER(REPLACE(c.member.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%')) AND c.checkoutStep = :checkoutState ORDER BY c.checkoutStep")
    Page<Checkout> findByMemberNameContainingWithoutSpaceAndCheckoutStep(@Param("withoutSpaceSearchText") String name, Pageable pageable, @Param("checkoutState") CheckoutState checkoutState);

    //공백 구분 없이 받는 분 이름 검색 + 주문상태
    @Query("SELECT c FROM Checkout c WHERE LOWER(REPLACE(c.checkoutName, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%')) AND c.checkoutStep = :checkoutState ORDER BY c.checkoutStep")
    Page<Checkout> findByCheckoutNameContainingWithoutSpaceAndCheckoutStep(@Param("withoutSpaceSearchText") String name, Pageable pageable, @Param("checkoutState") CheckoutState checkoutState);

    //공백 구분 없이 상품명 검색 + 주문상태
    @Query("SELECT DISTINCT c FROM Checkout c " +
            "JOIN c.checkoutDetailsList d " +
            "JOIN d.product p " +
            "WHERE LOWER(REPLACE(p.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:withoutSpaceSearchText, ' ', ''), '%')) AND c.checkoutStep = :checkoutState ORDER BY c.checkoutStep")
    Page<Checkout> findByProductNameContainingWithoutSpaceAndCheckoutStep(
            @Param("withoutSpaceSearchText") String productName,
            Pageable pageable, @Param("checkoutState") CheckoutState checkoutState
    );

    //주문 상태
    Page<Checkout> findByCheckoutStep(CheckoutState checkoutState,Pageable pageable);
}
