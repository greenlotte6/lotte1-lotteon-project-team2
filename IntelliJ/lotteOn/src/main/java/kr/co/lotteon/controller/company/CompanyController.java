package kr.co.lotteon.controller.company;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class CompanyController {

    @GetMapping("/company/index")
    public String index() {
        return "/company/index";
    }

    @GetMapping("/company/culture")
    public String culture() {
        return "/company/culture";
    }

    @GetMapping("/company/story")
    public String story() {
        return "/company/story";
    }

    @GetMapping("/company/recruit")
    public String recruit() {
        return "/company/employment";
    }

    @GetMapping("/company/media")
    public String media() {
        return "/company/media";
    }
}
