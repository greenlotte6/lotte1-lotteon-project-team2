package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    void deleteByProduct(Product product);

    void deleteByCartNo(int cartNo);

    Optional<Cart> findByCartNo(int cartNo);

    List<Cart> findAllByUser(User user);
}
