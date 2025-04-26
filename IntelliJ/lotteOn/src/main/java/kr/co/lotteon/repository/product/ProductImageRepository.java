package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    void deleteByProduct(Product product);

    Optional<ProductImage> findByProduct(Product savedProduct);
}
