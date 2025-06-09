package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.service.admin.ProductService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.product.ProductCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductCouponController {

    private final ProductCouponService productCouponService;

    // 쿠폰 정보
    @GetMapping("/product/coupon")
    @ResponseBody
    public List<CouponDTO> coupon(@RequestParam String company) {
        List<CouponDTO> couponDTOList = productCouponService.findAllByCompany(company);
        return couponDTOList;
    }


    // 쿠폰 - 인가 처리 리다이렉트
    @GetMapping("/product/ViewLoginCheck")
    public String viewLoginCheck(@RequestParam("prodNo") String prodNo) {
        return "redirect:/product/view?prodNo=" + prodNo;
    }


    // 쿠폰 발급받기
    @PostMapping("/product/couponIssue")
    @ResponseBody
    public ResponseEntity<String> couponIssue(@RequestParam  List<Long> cno, @AuthenticationPrincipal UserDetails userDetails) {
        int result = productCouponService.couponIssue(cno, userDetails);
        if (result == 1) {
            return ResponseEntity.ok("쿠폰이 발급되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("쿠폰 발급 중 오류가 발생했습니다.");
        }
    }


}
