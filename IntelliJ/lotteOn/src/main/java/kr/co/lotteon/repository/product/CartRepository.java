package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    void deleteByProduct(Product product);
}
