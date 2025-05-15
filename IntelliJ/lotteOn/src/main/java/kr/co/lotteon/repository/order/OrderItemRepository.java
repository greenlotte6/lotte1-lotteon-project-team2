package kr.co.lotteon.repository.order;

import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;

import kr.co.lotteon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {


    @Query("SELECT oi.category, SUM(oi.itemPrice * (1 - (oi.itemDiscount / 100.0))) " +
            "FROM OrderItem oi GROUP BY oi.category ORDER BY SUM(oi.itemPrice * (1 - (oi.itemDiscount / 100.0))) DESC")
    List<Object[]> findTotalPriceGroupByCategory();

    List<OrderItem> findAllByOrder_OrderNo(int orderNo);


    long countByOrderStatus(String state);


    Optional<OrderItem> findByItemNo(long itemNo);

    @Query("""
    SELECT 
        DATE(o.orderDate),
        oi.orderStatus,
        COUNT(oi)
    FROM OrderItem oi
    JOIN oi.order o
    WHERE o.orderDate >= :startDate
    GROUP BY DATE(o.orderDate), oi.orderStatus
    ORDER BY DATE(o.orderDate) ASC
    """)
    List<Object[]> countOrderStatsByDate(@Param("startDate") LocalDateTime startDate);


    @Query("""
    SELECT COUNT(oi)
    FROM OrderItem oi
    JOIN oi.order o
    WHERE FUNCTION('DATE', o.orderDate) = CURRENT_DATE
      AND oi.orderStatus = :state
    """)
    long countByOrderStatusToday(@Param("state") String state);

    List<OrderItem> findByOrder(Order order);


    Boolean existsByOrder_OrderDateBetweenAndOrderStatusAndProduct_ProdNameContaining(LocalDateTime start, LocalDateTime end, String searchType, String keyword);

    Boolean existsByOrder_OrderDateBetweenAndProduct_ProdNameContaining(LocalDateTime start, LocalDateTime end, String keyword);

    long countByOrder_User_UidAndOrderStatusNotIn(String uid, List<String> excluded);
}
