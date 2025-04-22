package kr.co.lotteon.controller.policy;

import kr.co.lotteon.dto.config.TermsDTO;
import kr.co.lotteon.service.user.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PolicyController {

    private final TermsService termsService;

    // 구매회원 약관
    @GetMapping("/policy/buyer")
    public String buyer(Model model) {
        TermsDTO termsDTO = termsService.findSplitPolicy();
        model.addAttribute("terms", termsDTO);
        return "/policy/buyer";
    }

    // 판매회원 이용 약관
    @GetMapping("/policy/seller")
    public String seller(Model model) {
        TermsDTO termsDTO = termsService.findSplitSeller();
        model.addAttribute("terms", termsDTO);
        return "/policy/seller";
    }

    // 전자금융거래 약관
    @GetMapping("/policy/finance")
    public String finance(Model model) {
        TermsDTO termsDTO = termsService.findSplitFinance();
        model.addAttribute("terms", termsDTO);
        return "/policy/finance";
    }

    // 위치정보 약관
    @GetMapping("/policy/location")
    public String location(Model model) {
        TermsDTO termsDTO = termsService.findSplitLocation();
        model.addAttribute("terms", termsDTO);
        return "/policy/location";
    }

    // 개인정보처리방침 약관
    @GetMapping("/policy/privacy")
    public String privacy(Model model) {
        TermsDTO termsDTO = termsService.findSplitPrivacy();
        model.addAttribute("terms", termsDTO);
        return "/policy/privacy";
    }

}
