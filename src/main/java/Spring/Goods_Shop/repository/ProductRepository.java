package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 카테고리
    Page<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
    // 검색어
    Page<Product> findByNameContaining(String name, Pageable pageable);

    List<Product>  findTop3ByOrderBySellingCountDescIdDesc();

    List<Product> findTop3ByOrderByIdDesc();
}
