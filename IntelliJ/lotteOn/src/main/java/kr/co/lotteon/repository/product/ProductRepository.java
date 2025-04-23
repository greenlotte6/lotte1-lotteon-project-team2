package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String>, ProductRepositoryCustom {
    List<Product> findByProdNoStartingWith(String prodNo);
}
