package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Checkout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout,Long> {
    Page<Checkout> findAll(Pageable pageable);

    List<Checkout> findAllByMemberId(Long memberId);

    @Query("SELECT c FROM Checkout c WHERE REPLACE(c.member.name, ' ', '') LIKE %:withoutSpaceSearchText%")
    Page<Checkout> findByMemberNameContainingWithoutSpace(@Param("withoutSpaceSearchText") String name, Pageable pageable);
}
