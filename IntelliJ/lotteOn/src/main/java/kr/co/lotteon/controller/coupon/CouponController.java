package kr.co.lotteon.controller.coupon;


import kr.co.lotteon.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CouponController {

    private final CouponService couponService;



}
