package kr.co.lotteon.controller.mypage;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MyController {

    private final MyPageService myPageService;
    private final UserRepository userRepository;


    @GetMapping("/my/home")
    public String myHome(HttpSession session, PageRequestDTO pageRequestDTO, Model model) {

        // 세션에서 로그인한 User 객체 꺼내기
        User writer = (User) session.getAttribute("loginUser");

        log.info("loginUser : " + writer);

        // 로그인한 유저의 문의글만 조회
        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(writer, pageRequestDTO);
        
        // 페이지 목록 5개로 제한
        pageRequestDTO.setSize(5);

        model.addAttribute("inquiryDTO", inquiryDTO);

        return "/myPage/myPageMain";
    }

    @GetMapping("/my/order")
    public String order() {
        return "/myPage/orderDetails";
    }

    @GetMapping("/my/point")
    public String point() {
        return "/myPage/pointDetails";
    }

    @GetMapping("/my/coupon")
    public String coupon() {return "/myPage/couponDetails";}

    @GetMapping("/my/review")
    public String review() {
        return "/myPage/myReview";
    }

    @GetMapping("/my/qna")
    public String qna(Principal principal, PageRequestDTO pageRequestDTO, Model model) {

        /*
        String uid = principal.getName();
        User writer = userRepository.findByUid(uid).orElseThrow(); // User 엔티티로 조회

        log.info("loginUser : " + writer);

        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(writer, pageRequestDTO);

        pageRequestDTO.setSize(10);

        model.addAttribute("inquiryDTO", inquiryDTO);
*/
        return "/myPage/myInquiry";
    }

    @GetMapping("/my/info")
    public String info() {
        return "/myPage/mySetting";
    }
}
