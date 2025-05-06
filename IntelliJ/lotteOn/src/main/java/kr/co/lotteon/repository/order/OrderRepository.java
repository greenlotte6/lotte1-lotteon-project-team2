package kr.co.lotteon.repository.order;

import kr.co.lotteon.dto.seller.SalesDTO;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.custom.OrderRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> , OrderRepositoryCustom {
    Page<Order> findAllByUser(User user, Pageable pageable);
    List<Order> findByUser(User user);


    @Query("""
    SELECT o.orderStatus AS status, COUNT(o) AS cnt 
    FROM OrderItem oi
    JOIN oi.order o
    JOIN oi.product p
    WHERE p.seller.sno = :sno
    GROUP BY o.orderStatus
    """)
    List<Object[]> findOrderStatusCountsBySeller(@Param("sno") int sno);
}
