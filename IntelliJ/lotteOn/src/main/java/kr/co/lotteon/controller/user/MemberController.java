package kr.co.lotteon.controller.user;

import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.config.Terms;
import kr.co.lotteon.service.user.TermsService;
import kr.co.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final UserService userService;
    private final TermsService termsService;



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

    @GetMapping("/member/register")
    public String register() {


        return "/member/register";
    }

    @PostMapping("/member/register")
    public String register(@ModelAttribute UserDTO userDTO, @RequestParam("phone") String phone) {

        userDTO.setHp(phone);
        log.info("▶ 회원가입 요청 데이터: {}", userDTO);

        userService.register(userDTO);
        return "redirect:/member/login";
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




}
