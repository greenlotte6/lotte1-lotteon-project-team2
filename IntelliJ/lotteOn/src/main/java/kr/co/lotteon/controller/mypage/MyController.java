package kr.co.lotteon.controller.mypage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MyController {

    @GetMapping("/my/home")
    public String myHome() {
        return "/myPage/myPageMain";
    }

    @GetMapping("/my/order")
    public String order() {
        return "/myPage/couponDetails";
    }

    @GetMapping("/my/point")
    public String point() {
        return "/myPage/pointDetails";
    }

    @GetMapping("/my/coupon")
    public String coupon() {
        return "/myPage/couponDetails";
    }

    @GetMapping("/my/review")
    public String review() {
        return "/myPage/myReview";
    }

    @GetMapping("/my/qna")
    public String qna() {
        return "/myPage/myInquiry";
    }

    @GetMapping("/my/info")
    public String info() {
        return "/myPage/mySetting";
    }
}
