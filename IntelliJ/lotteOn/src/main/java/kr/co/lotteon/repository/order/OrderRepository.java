package kr.co.lotteon.repository.order;

import kr.co.lotteon.entity.order.Order;
import kr.co.lotteon.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByUser(User user, Pageable pageable);

    List<Order> findByUser(User user);
}
