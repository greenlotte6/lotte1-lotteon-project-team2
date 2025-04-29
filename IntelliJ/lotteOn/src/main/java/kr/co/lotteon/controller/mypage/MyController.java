package kr.co.lotteon.controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.repository.user.UserRepository;
import kr.co.lotteon.service.mypage.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MyController {

    private final MyPageService myPageService;


    @GetMapping("/my/home")
    public String myHome(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // 페이지 목록 5개로 제한
        pageRequestDTO.setSize(5);
        
        // 세션 정보 가져오기
        String writer = userDetails.getUsername();

        log.info("writer : " + writer);
        
        // 로그인 유저 조회
        UserDTO userDTO = myPageService.findByUid(writer);

        // 로그인한 유저의 문의글만 조회
        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(userDTO, pageRequestDTO);
        



        model.addAttribute("inquiryDTO", inquiryDTO);

        return "/myPage/myPageMain";
    }

    @GetMapping("/my/order")
    public String order() {
        return "/myPage/orderDetails";
    }

    @GetMapping("/my/point")
    public String point(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String uid = userDetails.getUsername();
        log.info("uid : " + uid);

        UserDTO userDTO = myPageService.findByUid(uid);

        PageResponseDTO<PointDTO> pointDTO = myPageService.pointFindAll(userDTO, pageRequestDTO);
        log.info("pointDTO : " + pointDTO);

        model.addAttribute("pointDTO", pointDTO);

        return "/myPage/pointDetails";
    }

    @GetMapping("/my/coupon")
    public String coupon() {return "/myPage/couponDetails";}

    @GetMapping("/my/review")
    public String review(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {


        // 세션 정보 가져오기
        String writer = userDetails.getUsername();
        log.info("writer : " + writer);

        // 로그인한 유저의 문의글만 조회
        PageResponseDTO<ReviewDTO> reviewDTO = myPageService.reviewFindAll(writer, pageRequestDTO);


        System.out.println(reviewDTO);
        model.addAttribute("reviewDTO", reviewDTO);

        return "/myPage/myReview";
    }

    @GetMapping("/my/qna")
    public String qna(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String writer = userDetails.getUsername();



        UserDTO userDTO = myPageService.findByUid(writer);

        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(userDTO, pageRequestDTO);

        model.addAttribute("inquiryDTO", inquiryDTO);

        return "/myPage/myInquiry";
    }

    @GetMapping("/my/info")
    public String info() {
        return "/myPage/mySetting";
    }
}
