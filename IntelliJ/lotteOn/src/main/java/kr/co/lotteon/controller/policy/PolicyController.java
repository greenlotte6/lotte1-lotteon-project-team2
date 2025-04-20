package kr.co.lotteon.controller.policy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PolicyController {

    // 구매회원 약관
    @GetMapping("/policy/buyer")
    public String buyer() {
        return "/policy/buyer";
    }

    // 판매회원 이용 약관
    @GetMapping("/policy/seller")
    public String seller() {
        return "/policy/seller";
    }

    // 전자금융거래 약관
    @GetMapping("/policy/finance")
    public String finance() {
        return "/policy/finance";
    }

    // 위치정보 약관
    @GetMapping("/policy/location")
    public String location() {
        return "/policy/location";
    }

    // 개인정보처리방침 약관
    @GetMapping("/policy/privacy")
    public String privacy() {
        return "/policy/privacy";
    }



}
