package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.dto.checkout.CheckoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutDetailsRepository extends JpaRepository<CheckoutDetails,Long> {
   List<CheckoutDetails> findALLByCheckoutId(Long checkoutId);
}
