package kr.co.lotteon.repository.coupon;

import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponIssueRepository extends JpaRepository<CouponIssue, Integer> {
    boolean existsByUserAndCoupon(User user, Coupon coupon);

    List<CouponIssue> findByUserAndCoupon(User user, Coupon coupon);
}
