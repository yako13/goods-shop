package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
    Optional<Delivery> findByMemberIdAndDefaultDelivery (Long memberId, boolean defaultDelivery);

    List<Delivery> findAllByMemberId(Long memberId);
}
