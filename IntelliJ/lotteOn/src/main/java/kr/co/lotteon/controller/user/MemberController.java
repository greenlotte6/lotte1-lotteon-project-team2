package kr.co.lotteon.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.seller.SellerDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.entity.seller.Seller;
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

    @PostMapping("/member/login")
    public String login(@RequestParam String uid,
                        @RequestParam String pass,
                        @RequestParam(required = false) String autoLogin,
                        HttpServletResponse response,
                        HttpSession session,
                        Model model) {

        log.info("▶ [로그인 시도] uid: {}", uid);
        log.info("▶ [전송된 autoLogin 값] = {}", autoLogin);

        Optional<User> userOpt = userService.findByUidAndPass(uid, pass);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("sessUser", user);
            log.info("✅ 로그인 성공 - uid: {}", uid);

            if (autoLogin != null) {
                if ("true".equals(autoLogin)) {
                    log.info("✅ 자동 로그인 활성화됨 - 쿠키 생성 시작");
                    Cookie cookie = new Cookie("autoLogin", user.getUid());
                    cookie.setMaxAge(60 * 60 * 24 * 7); // 7일 유지
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    log.info("✅ 자동 로그인 쿠키 생성 완료 (uid: {})", user.getUid());
                } else {
                    log.info("❕ autoLogin 값이 'true'가 아님 → 쿠키 생성 안함 (현재 값: {})", autoLogin);
                }
            } else {
                log.info("❌ autoLogin 파라미터가 전송되지 않음 (체크박스 미선택)");
            }

            return "redirect:/"; // 메인으로 이동
        } else {
            log.warn("❌ 로그인 실패 - 아이디 또는 비밀번호 불일치: uid={}", uid);
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "/member/login";
        }
    }

    // 로그아웃 처리 - 자동 로그인 쿠키 제거
    @GetMapping("/member/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        Cookie cookie = new Cookie("autoLogin", null);
        cookie.setMaxAge(0); // 즉시 만료
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
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
                                  HttpSession session,
                                  Model model) {

        log.info("▶ 이메일 인증 요청: {}", email);
        log.info("▶ 인증코드: {}", authCode);
        String resetEmail = (String) session.getAttribute("resetEmail");

        log.info("▶ 세션 저장된 인증 이메일: {}", resetEmail);

        // ✅ [1] 비밀번호 재설정 흐름인 경우
        if (resetEmail != null && resetEmail.equals(email)) {
            if (session.getAttribute("resetUid") != null) {
                session.setAttribute("findUid", session.getAttribute("resetUid"));
                session.removeAttribute("resetUid");
                session.removeAttribute("resetEmail");
                log.info("✅ 비밀번호 재설정 요청 - resetPass로 이동");
                return "redirect:/user/member/resetPass";
            }
        }

        // ✅ [2] 일반적인 아이디 찾기 흐름 (resetEmail 없어도 실행)
        Optional<User> userOpt = userService.findByEmail(email);
        log.info("▶ 유저 존재 여부: {}", userOpt.isPresent());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            model.addAttribute("name", user.getName());
            model.addAttribute("uid", user.getUid());
            model.addAttribute("regDate", user.getRegDate());

            log.info("✅ 일반 아이디 찾기 - findIdResult로 이동");
            return "/member/findIdResult";
        }

        log.warn("❌ 인증 실패 또는 이메일이 존재하지 않음");
        model.addAttribute("error", "인증에 실패했거나 존재하지 않는 이메일입니다.");
        return "/member/EmailAuth";
    }

    @PostMapping("/member/password/emailAuth")
    public String passwordEmailAuth(@RequestParam("uid") String uid,
                                    HttpSession session,
                                    Model model) {
        Optional<User> userOpt = userService.findByUid(uid);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            session.setAttribute("resetUid", user.getUid());    // UID 저장
            session.setAttribute("resetEmail", user.getEmail()); // 이메일 저장
            return "redirect:/user/member/EmailAuth";
        } else {
            model.addAttribute("error", "존재하지 않는 아이디입니다.");
            return "/member/findAccount";
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


    // 판매회원 - 사업자등록번호로 아이디 찾기
    @PostMapping("/member/findIdResult/seller")
    public String findSellerIdByBizNo(@RequestParam("bizNum") String bizNum,
                                      Model model) {
        Optional<Seller> sellerOpt = sellerService.findByBizRegNo(bizNum);
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            User user = seller.getUser(); // Seller 엔티티에 매핑된 User 엔티티를 가져온다.

            model.addAttribute("name", user.getName());
            model.addAttribute("uid", user.getUid());
            model.addAttribute("regDate", user.getRegDate());
            return "/member/findIdResult";  // 성공 시 아이디 결과 페이지로 이동
        } else {
            model.addAttribute("error", "일치하는 판매회원이 없습니다.");
            return "/member/findAccount"; // 실패 시 다시 찾기 페이지로
        }
    }


    // 판매회원 - 비밀번호 재설정 단계
    @PostMapping("/member/resetPass/seller")
    public String resetSellerPass(@RequestParam String company,
                                  @RequestParam String bizRegNo,
                                  HttpSession session,
                                  Model model) {
        Optional<Seller> sellerOpt = sellerService.findByCompanyAndBizRegNo(company, bizRegNo);

        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            User user = seller.getUser();

            session.setAttribute("findUid", user.getUid()); // UID 세션 저장
            return "redirect:/user/member/resetPass";
        } else {
            model.addAttribute("error", "일치하는 판매자 정보가 없습니다.");
            return "/member/findAccount";
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

    @PostMapping("/member/resetPass")
    public String updateSellerPassword(@RequestParam("newPassword") String newPassword,
                                       HttpSession session,
                                       Model model) {
        String uid = (String) session.getAttribute("findUid");
        if (uid == null) {
            model.addAttribute("error", "인증된 사용자가 없습니다.");
            return "/member/resetPass";
        }

        sellerService.updateSellerPassword(uid, newPassword);
        session.removeAttribute("findUid");

        return "redirect:/user/member/login";
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