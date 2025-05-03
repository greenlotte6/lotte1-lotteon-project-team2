package kr.co.lotteon.repository.product;

import kr.co.lotteon.entity.cart.Cart;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    void deleteByProduct(Product product);

    void deleteByCartNo(int cartNo);

    Optional<Cart> findByCartNo(int cartNo);

    List<Cart> findAllByUser(User user);

    @Query("SELECT c FROM Cart c WHERE c.user.uid = :uid AND c.product.prodNo = :prodNo " +
            "AND c.opt1 = :opt1 AND c.opt1Cont = :opt1Cont " +
            "AND c.opt2 = :opt2 AND c.opt2Cont = :opt2Cont " +
            "AND c.opt3 = :opt3 AND c.opt3Cont = :opt3Cont " +
            "AND c.opt4 = :opt4 AND c.opt4Cont = :opt4Cont " +
            "AND c.opt5 = :opt5 AND c.opt5Cont = :opt5Cont " +
            "AND c.opt6 = :opt6 AND c.opt6Cont = :opt6Cont")
    Optional<Cart> findByUserAndProductAndOptions(
            @Param("uid") String uid,
            @Param("prodNo") String prodNo,
            @Param("opt1") String opt1, @Param("opt1Cont") String opt1Cont,
            @Param("opt2") String opt2, @Param("opt2Cont") String opt2Cont,
            @Param("opt3") String opt3, @Param("opt3Cont") String opt3Cont,
            @Param("opt4") String opt4, @Param("opt4Cont") String opt4Cont,
            @Param("opt5") String opt5, @Param("opt5Cont") String opt5Cont,
            @Param("opt6") String opt6, @Param("opt6Cont") String opt6Cont
    );

}
