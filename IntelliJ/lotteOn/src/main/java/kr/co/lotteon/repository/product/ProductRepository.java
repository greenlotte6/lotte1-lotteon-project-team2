package kr.co.lotteon.repository.product;

import com.querydsl.core.BooleanBuilder;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String>, ProductRepositoryCustom {
    List<Product> findByProdNoStartingWith(String select);

    Optional<Product> findByProdNo(String prodNo);

    List<Product> findBySubCategory(SubCategory subCategory);

    List<Product> findTop8ByOrderByProdDiscountDesc();

    List<Product> findTop8ByOrderByHitDesc();

    List<Product> findTop8ByOrderByRatingAvgDesc();

    List<Product> findTop8ByOrderByReviewCountDesc();

    List<Product> findTop5ByOrderByProdSoldDesc();

    List<Product> findTop8ByOrderByRatingTotalDesc();

    List<Product> findTop8ByOrderByRegDateDesc();

    List<Product> findAll(BooleanBuilder builder);
}
