package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HanDeliveryRepository extends JpaRepository<Delivery,Long> {

    // 유저 pk로 기본배송지(defaultAdd)가 true(1)인 주소록 조회
    Delivery findByMemberIdAndDefaultDelivery(Long Id, boolean defaultDelivery);
}
