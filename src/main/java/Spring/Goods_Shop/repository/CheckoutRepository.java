package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Checkout;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutRepository extends JpaRepository<Checkout,Long> {
    Page<Checkout> findAll(Pageable pageable);

    List<Checkout> findAllByMemberId(Long memberId);
}
