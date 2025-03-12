package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayRepository extends JpaRepository<Pay,Long> {
    Optional<Pay> findByMemberIdAndDefaultCard (Long memberId, boolean defaultCard);
}
