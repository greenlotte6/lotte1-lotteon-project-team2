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

import java.time.LocalDateTime;
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

    @Query("""
    SELECT ROUND(SUM(
        oi.itemPrice * oi.itemCount * (1 - oi.itemDiscount / 100.0)
    ))
    FROM OrderItem oi
    JOIN oi.order o
    JOIN oi.product p
    WHERE p.seller.sno = :sno AND o.orderStatus = '구매확정'
    """)
    Long findConfirmedSalesTotalBySeller(@Param("sno") int sno);

    // 날짜 별 주문 총량
    @Query("""
    SELECT o.orderStatus AS status, COUNT(o) AS cnt 
    FROM OrderItem oi
    JOIN oi.order o
    JOIN oi.product p
    WHERE p.seller.sno = :sno
      AND o.orderDate >= :term
    GROUP BY o.orderStatus
    """)
    List<Object[]> findOrderStatusCountsBySellerAndDate(
            @Param("sno") int sno,
            @Param("term") LocalDateTime term
    );;

    // 날짜 별 매출합계
    @Query("""
    SELECT ROUND(SUM(
        oi.itemPrice * oi.itemCount * (1 - oi.itemDiscount / 100.0)
    ))
    FROM OrderItem oi
    JOIN oi.order o
    JOIN oi.product p
    WHERE p.seller.sno = :sno AND o.orderStatus = '구매확정'
        AND o.orderDate >= :term
    """)
    Long findConfirmedSalesTotalBySellerAndDate(int sno, LocalDateTime term);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    long countByorderDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
