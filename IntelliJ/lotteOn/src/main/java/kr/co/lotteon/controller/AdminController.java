package kr.co.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class AdminController {

    // 관리자 메인
    @GetMapping("/admin")
    public String main() {
        return "/admin/main";
    }

}
