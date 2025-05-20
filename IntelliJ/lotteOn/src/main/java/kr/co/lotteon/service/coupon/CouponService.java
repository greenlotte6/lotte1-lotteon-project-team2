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

import javax.swing.text.html.Option;
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

    private final CouponRepository couponRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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

    public List<CouponIssueDTO> findAllByUser(String uid) {
        Optional<User> optUser = userRepository.findByUid(uid);
        if (optUser.isEmpty()) {
            return null;
        }
        User user = optUser.get();

        List<CouponIssue> couponIssueList = couponIssueRepository.findAllByUser(user);

        List<CouponIssueDTO> couponIssueDTOList = couponIssueList.stream()
                .filter(couponIssue -> couponIssue.getValidTo() != null &&
                        LocalDateTime.parse(couponIssue.getValidTo(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                .isAfter(LocalDateTime.now()))
                .map(couponIssue -> modelMapper.map(couponIssue, CouponIssueDTO.class))
                .collect(Collectors.toList());

        return couponIssueDTOList;
    }


    public int couponIssue(CouponIssueDTO couponIssueDTO, UserDetails userDetails) {

        User user = User.builder()
                .uid(userDetails.getUsername())
                .build();

        Coupon coupon = Coupon.builder()
                .cno(Long.parseLong(couponIssueDTO.getCno()))
                .build();

        CouponIssue couponIssue = modelMapper.map(couponIssueDTO, CouponIssue.class);

        couponIssue.setUser(user);
        couponIssue.setCoupon(coupon);

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

    public void changeState(Long issueNo) {

        if (issueNo == null || issueNo == 0L) {
            log.info("쿠폰이 선택되지 않아 상태 변경 생략. issueNo: {}", issueNo);
            return;
        }

        Optional<CouponIssue> optCouponIssue = couponIssueRepository.findById(issueNo);

        if(optCouponIssue.isPresent()) {
            CouponIssue couponIssue = optCouponIssue.get();
            couponIssue.setState("사용");
            couponIssue.setUsedDate(LocalDate.now());
            couponIssueRepository.save(couponIssue);
        }
        else {
            log.error("Coupon not found for issueNo: {}", issueNo);
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

        /*
        Coupon coupon = Coupon.builder()
                .cno(cno)
                .build();
*/

        /*
        CouponIssue couponIssue = CouponIssue.builder()
                .coupon(coupon)
                .user(user)
                .issuedBy(coupon.getIssuedBy())
                .build();
*/

    }
}
