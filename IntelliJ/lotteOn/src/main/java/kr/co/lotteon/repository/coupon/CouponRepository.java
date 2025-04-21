package kr.co.lotteon.repository.coupon;


import kr.co.lotteon.entity.coupon.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponIssue,Integer> {

}
