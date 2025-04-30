package kr.co.lotteon.service.coupon;

import jakarta.transaction.Transactional;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final CouponIssueRepository couponIssueRepository;

    public List<CouponDTO> findAllByCompany(String company) {
        List<Coupon> couponList = couponRepository.findAllByIssuedBy(company);
        LocalDate today = LocalDate.now();

        List<Coupon> validCouponList = couponList.stream()
                .filter(coupon -> coupon.getValidTo().isAfter(today) || coupon.getValidTo().isEqual(today))
                .collect(Collectors.toList());

        List<CouponDTO> couponDTOList = validCouponList.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());

        return couponDTOList;
    }

    public int  couponIssue(CouponIssueDTO couponIssueDTO, UserDetails userDetails) {

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        Coupon coupon = Coupon.builder()
                .cno(Long.parseLong(couponIssueDTO.getCno()))
                .build();

        CouponIssue couponIssue = modelMapper.map(couponIssueDTO, CouponIssue.class);

        couponIssue.setUser(user);
        couponIssue.setCoupon(coupon);

        log.info("couponIssue: " + couponIssue);

        Boolean exist = couponIssueRepository.existsByUserAndCoupon(user, coupon);

        try {
            if(!exist){
                couponIssueRepository.save(couponIssue);
            }
            return 1;
        }catch (Exception e) {
            return 0;
        }
    }


    public void IssueToUser(CouponDTO couponDTO, UserDetails userDetails) {
        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        Optional<Coupon> couponOpt = couponRepository.findById(couponDTO.getCno());
        if (couponOpt.isPresent()) {

            Coupon coupon = couponOpt.get();

            CouponIssue couponIssue = CouponIssue.builder()
                    .coupon(coupon)
                    .user(user)
                    .issuedBy(coupon.getIssuedBy())
                    .build();

            Boolean exist = couponIssueRepository.existsByUserAndCoupon(user, coupon);

            // 쿠폰이 있을때만 등록
            if(!exist){
                // couponIssue.setEndDate(LocalDateTime.now().plusDays(coupon.getValidDaysAfterIssue()));
                couponIssueRepository.save(couponIssue);
            }

        }

    }
}
