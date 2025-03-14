package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
    Page<Product> findByNameContaining(String name, Pageable pageable);
}
