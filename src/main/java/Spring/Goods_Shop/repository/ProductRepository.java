package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
