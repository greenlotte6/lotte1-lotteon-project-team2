package kr.co.lotteon.repository.order;

import kr.co.lotteon.dto.order.OrderInfoDTO;
import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.order.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT new kr.co.lotteon.dto.order.OrderInfoDTO(" +
            "o.orderDate, o.orderNo, o.orderStatus, " +
            "p.prodName, s.company, oi.itemCount, oi.itemPrice, pi.oNameList) " +
            "FROM OrderItem oi " +
            "JOIN oi.order o " +
            "JOIN oi.product p " +
            "JOIN p.seller s " +
            "JOIN p.productImage pi " +
            "WHERE o.user.uid = :userId")
    Page<OrderInfoDTO> findOrderInfoByUserId(@Param("userId") String userId, Pageable pageable);
}
