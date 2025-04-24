package kr.co.lotteon.repository.coupon;


import kr.co.lotteon.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    List<Coupon> findAllByIssuedBy(String company);
}
