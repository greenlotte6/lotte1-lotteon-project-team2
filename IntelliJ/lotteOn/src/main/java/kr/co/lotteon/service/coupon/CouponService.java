package kr.co.lotteon.service.coupon;

import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponIssueRepository couponIssueRepository;


    public void IssueToUser(Integer index, UserDetails userDetails) {

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        int cno = 0;
        switch (index) {
            case 1: cno = 1012211372; break;
            case 2: cno = 1012211373; break;
            case 3: cno = 1012211370; break;
            case 4: cno = 1012211371; break;
            default: break;
        }

        Coupon coupon = Coupon.builder()
                .cno(cno)
                .build();

        Boolean exist = couponIssueRepository.existsByUserAndCoupon(user, coupon);
        LocalDateTime now = LocalDateTime.now().plusDays(20);

        if(!exist){
            CouponIssue couponIssue = CouponIssue
                    .builder()
                    .user(user)
                    .coupon(coupon)
                    .validTo(String.valueOf(now))
                    .state("미사용")
                    .issuedBy("관리자")
                    .build();

            couponIssueRepository.save(couponIssue);
        }
    }
}
