package kr.co.lotteon.repository.order;

import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
