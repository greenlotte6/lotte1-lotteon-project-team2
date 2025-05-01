package kr.co.lotteon.controller.coupon;


import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/coupon")
@Controller
public class CouponController {

    private final CouponService couponService;

    // 쿠폰 발급
    @ResponseBody
    @GetMapping("/issue")
    public void issue(@AuthenticationPrincipal UserDetails userDetails, CouponDTO couponDTO) {
        couponService.IssueToUser(couponDTO, userDetails);
    }



}
