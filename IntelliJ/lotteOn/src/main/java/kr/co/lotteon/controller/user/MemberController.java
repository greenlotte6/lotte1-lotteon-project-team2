package kr.co.lotteon.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.service.seller.SellerService;
import kr.co.lotteon.service.user.TermsService;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import kr.co.lotteon.entity.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final UserService userService;
    private final TermsService termsService;
    private final SellerService sellerService;



    @GetMapping("/member/login")
    public String login() {
        return "/member/login";
    }

    @GetMapping("/member/join")
    public String join() {
        return "/member/join";
    }

    @GetMapping("/member/signup")
    public String signup(Model model) {

        Terms terms = termsService.getTerms(); // 인스턴스 명은 소문자!
        model.addAttribute("terms", terms);
        return "/member/signup";
    }

    @GetMapping("/member/sellerSignup")
    public String sellerSignup(Model model) {

        Terms terms = termsService.getTerms();
        model.addAttribute("terms", terms);
        return "/member/sellerSignup";
    }



    @GetMapping("/member/register")
    public String register() {




        return "/member/register";
    }

    @PostMapping("/member/register")
    public String register(@ModelAttribute UserDTO userDTO, @RequestParam("phone") String phone,  HttpServletRequest req) {

        userDTO.setHp(phone);
        log.info("▶ 회원가입 요청 데이터: {}", userDTO);

        String regip = req.getRemoteAddr();
        userDTO.setRegip(regip);

        userService.register(userDTO);
        return "redirect:/user/member/login";
    }

    @GetMapping("/user/checkUid")
    @ResponseBody
    public boolean checkUid(@RequestParam("uid") String uid) {
        return userService.checkUid(uid);
    }

    @GetMapping("/member/registerSeller")
    public String registerSeller() {
        return "/member/registerSeller";
    }


    @PostMapping("/member/registerSeller")
    public String registerSeller(@ModelAttribute UserDTO userDTO,
                                 @ModelAttribute SellerDTO sellerDTO,
                                 @RequestParam("phone") String phone,
                                 HttpServletRequest req) {

        userDTO.setHp(phone); // 전화번호 세팅
        log.info("▶ 회원가입 요청 데이터: {}", userDTO);
        log.info("▶ 판매자 정보 데이터: {}", sellerDTO);

        sellerService.saveSeller(userDTO, sellerDTO);

        return "redirect:/user/member/login";
    }


    @GetMapping("/member/EmailAuth")
    public String EmailAuth() {
        return "/member/EmailAuth";
    }

    @GetMapping("/member/findAccount")
    public String findAccount() {
        return "/member/findAccount";
    }

    @GetMapping("/member/findIdResult")
    public String findIdResult() {
        return "/member/findIdResult";
    }


    @PostMapping("/member/findIdResult")
    public String findUserId(@RequestParam String name,
                             @RequestParam String hp,
                             Model model) {
        Optional<User> userOpt = userService.findByNameAndHp(name, hp);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("name", user.getName());
            model.addAttribute("uid", user.getUid());
            model.addAttribute("regDate", user.getRegDate());
            return "/member/findIdResult";
        } else {
            model.addAttribute("error", "일치하는 회원이 없습니다.");
            return "/member/phoneAuth";
        }
    }
    @GetMapping("/member/phoneAuth")
    public String phoneAuth() {
        return "/member/phoneAuth";
    }

    @GetMapping("/member/resetPass")
    public String resetPass() {
        return "/member/resetPass";
    }


}
