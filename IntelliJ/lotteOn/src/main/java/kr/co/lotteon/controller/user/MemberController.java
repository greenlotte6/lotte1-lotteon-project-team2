package kr.co.lotteon.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.service.seller.SellerService;
import kr.co.lotteon.service.user.EmailService;
import kr.co.lotteon.service.user.TermsService;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import kr.co.lotteon.entity.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final UserService userService;
    private final TermsService termsService;
    private final SellerService sellerService;
    private final EmailService emailService;



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

    @PostMapping("/member/EmailAuth")
    public String emailAuthSubmit(@RequestParam String email,
                                  @RequestParam String authCode,
                                  Model model) {
        // 1. 서버에서 email, authCode 검증하는 로직 (필요하면)

        // 2. 검증 완료 → DB에서 email로 회원 찾기
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("name", user.getName());
            model.addAttribute("uid", user.getUid());
            model.addAttribute("regDate", user.getRegDate());
            return "/member/findIdResult"; // 결과 뷰로 이동
        } else {
            model.addAttribute("error", "일치하는 회원이 없습니다.");
            return "/member/EmailAuth"; // 다시 이메일 입력 페이지
        }
    }

    @GetMapping("/member/findAccount")
    public String findAccount() {
        return "/member/findAccount";
    }

    @GetMapping("/member/findIdResult")
    public String findIdResult() {
        return "/member/findIdResult";
    }


    // 폰인증
    @PostMapping("/member/findIdResult/phone")
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

    //이메일 인증
    @PostMapping("/member/findIdResult/email")
    public String findIdByEmail(Model model) {

        return "/member/findIdResult";
    }


    @GetMapping("/member/phoneAuth")
    public String phoneAuth() {
        return "/member/phoneAuth";
    }

    @GetMapping("/member/resetPass")
    public String resetPass() {
        return "/member/resetPass";
    }

    @PostMapping("/member/sendEmailAuth")
    @ResponseBody
    public Map<String, Object> sendEmailAuth(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        String authCode = emailService.sendAuthCode(email);

        Map<String, Object> result = new HashMap<>();
        if (authCode != null) {
            result.put("status", "success");
            result.put("authCode", authCode);
        } else {
            result.put("status", "fail");
            result.put("message", "이메일 발송 실패");
        }
        return result;
    }


}
