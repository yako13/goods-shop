package Spring.Goods_Shop.checkoutDetails;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutDetailsRepository extends JpaRepository<CheckoutDetails,Long> {
   List<CheckoutDetails> findALLByCheckoutId(Long checkoutId);
}
