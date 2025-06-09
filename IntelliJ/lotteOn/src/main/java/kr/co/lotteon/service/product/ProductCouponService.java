package kr.co.lotteon.service.product;

import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.user.UserDetailsRepository;
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
public class ProductCouponService {

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final ModelMapper modelMapper;

    // 쿠폰 정보
    public List<CouponDTO> findAllByCompany(String company) {
        List<Coupon> couponList = couponRepository.findValidCouponsByCompanyOrAdmin(company, LocalDate.now());

        return couponList.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());
    }


    // 쿠폰 발급받기
    public int couponIssue(List<Long> cnoList, UserDetails userDetails) {
        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        int successCount = 0;

        for (Long cno : cnoList) {
            Optional<Coupon> optionalCoupon = couponRepository.findById(cno);

            if (optionalCoupon.isEmpty()) {
                continue;
            }

            Coupon coupon = optionalCoupon.get();

            boolean exist = couponIssueRepository.existsByUserAndCoupon(user, coupon);
            if (exist) {
                successCount++;
                continue;
            }

            try {
                CouponIssue couponIssue = CouponIssue.builder()
                        .user(user)
                        .coupon(coupon)
                        .regDate(LocalDate.now())
                        .issuedBy(coupon.getIssuedBy())
                        .validTo(String.valueOf(coupon.getValidTo()))
                        .build();

                couponIssueRepository.save(couponIssue);
                successCount++;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return successCount;
    }


}
