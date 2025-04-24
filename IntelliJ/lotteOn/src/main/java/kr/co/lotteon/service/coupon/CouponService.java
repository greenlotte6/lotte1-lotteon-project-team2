package kr.co.lotteon.service.coupon;

import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.coupon.CouponIssue;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.coupon.CouponRepository;
import kr.co.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    public List<CouponDTO> findAllByCompany(String company) {
        List<Coupon> couponList = couponRepository.findAllByIssuedBy(company);

        List<CouponDTO> couponDTOList = couponList.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());

        return couponDTOList;
    }




}
