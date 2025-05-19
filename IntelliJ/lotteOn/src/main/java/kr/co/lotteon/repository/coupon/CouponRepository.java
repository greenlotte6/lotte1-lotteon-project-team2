package kr.co.lotteon.repository.coupon;

import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.point.Point;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.custom.CouponRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>, CouponRepositoryCustom {
    List<Coupon> findAllByIssuedBy(String company);

    Page<Coupon> findAllByUserAndValidToAfter(User user, LocalDate today, Pageable pageable);

    long countByUserAndValidToAfter(User user, LocalDate today);

    List<Coupon> findAllByUser(User user);
}
