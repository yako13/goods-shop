package Spring.Goods_Shop.repository;

import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 카테고리
    Page<Product> findByProductCategory(ProductCategory productCategory, Pageable pageable);
    // 검색어 db에 저장된 상품명의 공백을 지우고 반환
    @Query("SELECT p FROM Product p WHERE REPLACE(p.name, ' ', '') LIKE %:keyword%")
    Page<Product> findByNameContaining(@Param("keyword") String name, Pageable pageable);

    List<Product>  findTop4ByOrderBySellingCountDescIdDesc();

    List<Product> findTop4ByOrderByIdDesc();
}
