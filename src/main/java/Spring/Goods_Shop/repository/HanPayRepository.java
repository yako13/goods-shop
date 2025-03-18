package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HanPayRepository extends JpaRepository<Pay,Long> {

    // 유저 pk로 기본결제카드(defaultCard)가 true(1)인 주소록 조회
    Pay findByMemberIdAndDefaultCard(Long Id, boolean defaultCard);


}
