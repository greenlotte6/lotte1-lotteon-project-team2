package kr.co.lotteon.controller.mypage;

import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.feedback.ExchangeDTO;
import kr.co.lotteon.dto.feedback.ReturnDTO;
import kr.co.lotteon.dto.feedback.ReviewDTO;
import kr.co.lotteon.dto.order.OrderInfoDTO;
import kr.co.lotteon.dto.order.OrderItemDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.point.PointDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.mypage.MyPageService;
import kr.co.lotteon.service.order.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MyController {

    private final MyPageService myPageService;
    private final ConfigService configService;

    @GetMapping("/my/home")
    public String myHome(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        BannerDTO banner = configService.findBanner("MY1");
        model.addAttribute("banner", banner);


        // 페이지 목록 5개로 제한
        pageRequestDTO.setSize(5);
        
        // 세션 정보 가져오기
        String uid = userDetails.getUsername();

        // 로그인 유저 조회
        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        // 로그인한 유저의 포인트 목록 조회
        PageResponseDTO<PointDTO> pointDTO = myPageService.pointFindAll(userDTO, pageRequestDTO);

        // 로그인한 유저의 문의하기 조회
        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(userDTO, pageRequestDTO);

        // 로그인한 유저의 리뷰 조회
        PageResponseDTO<ReviewDTO> reviewDTO = myPageService.reviewFindAll(userDTO, pageRequestDTO);

        // 로그인한 유저의 쿠폰 조회
        PageResponseDTO<CouponDTO> couponDTO = myPageService.couponFindAll(userDTO, pageRequestDTO);

        // 로그인한 유저의 주문내역 조회
        PageResponseDTO<OrderInfoDTO> orderInfoPagingDTO = myPageService.orderInfoPaging(pageRequestDTO, uid);

        // 주문내역 모달





        // 로그인한 유저의 정보 조회
        myPageService.splitPhone(userDTO);
        String formattedPhone = myPageService.joinPhone(userDTO);
        userDTO.setHp(formattedPhone);

        model.addAttribute("pointDTO", pointDTO);
        model.addAttribute("inquiryDTO", inquiryDTO);
        model.addAttribute("reviewDTO", reviewDTO);
        model.addAttribute("couponDTO", couponDTO);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("point", point);
        model.addAttribute("orderInfoPagingDTO", orderInfoPagingDTO);


        return "/myPage/myPageMain";
    }

    @GetMapping("/my/order")
    public String order(@AuthenticationPrincipal UserDetails userDetails, PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(10);

        String uid = userDetails.getUsername();

        PageResponseDTO<OrderInfoDTO> orderInfoPagingDTO = myPageService.orderInfoPaging(pageRequestDTO, uid);
        //PageResponseDTO<ReturnDTO> returnDTO = myPageService.returnRegister();


        log.info("orderInfoPagingDTO : " + orderInfoPagingDTO);



        model.addAttribute("orderInfoPagingDTO", orderInfoPagingDTO);


        return "/myPage/orderDetails";


    }

    @GetMapping("/my/point")
    public String point(PageRequestDTO pageRequestDTO,
                        Model model,
                        @RequestParam(value="startDate", required = false, defaultValue = "") String startDate,
                        @RequestParam(value="endDate", required = false, defaultValue = "") String endDate,
                        @RequestParam(value = "search", required = false, defaultValue = "") String search,
                        @AuthenticationPrincipal UserDetails userDetails) {


        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        //PageResponseDTO<PointDTO> pointDTO = myPageService.searchPoint(userDTO, pageRequestDTO , startDate, endDate, search);

        PageResponseDTO<PointDTO> pointDTO = myPageService.pointFindAll(userDTO, pageRequestDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("pointDTO", pointDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);

        return "/myPage/pointDetails";
    }

    @GetMapping("/my/coupon")
    public String coupon(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        PageResponseDTO<CouponDTO> couponDTO = myPageService.couponFindAll(userDTO, pageRequestDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("couponDTO", couponDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);

        return "/myPage/couponDetails";
    }

    @GetMapping("/my/review")
    public String review(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {


        // 세션 정보 가져오기
        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 문의글만 조회
        PageResponseDTO<ReviewDTO> reviewDTO = myPageService.reviewFindAll(userDTO, pageRequestDTO);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("reviewDTO", reviewDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);

        return "/myPage/myReview";
    }

    @GetMapping("/my/qna")
    public String qna(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String writer = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(writer);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(userDTO, pageRequestDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("inquiryDTO", inquiryDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);


        return "/myPage/myInquiry";
    }
    
    
    // 구매확정 모달
    @PostMapping("/my/order/confirm")
    public ResponseEntity<?> confirmPurchase(@RequestBody Map<String, Object> payload) {
        Long itemNo = Long.valueOf(payload.get("itemNo").toString());

        boolean result = myPageService.confirmPurchase(itemNo);
        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("구매확정 실패");
        }
    }
    
    // 반품하기 모달
    @PostMapping("/my/order/return")
    public ResponseEntity<?> returnRequest(@RequestParam("type") String type,
                                           @RequestParam("itemNo") Long itemNo,
                                           @RequestParam("content") String content,
                                           @RequestParam("uploadType") String uploadType,
                                           @RequestParam(value = "file", required = false) MultipartFile file,
                                           @AuthenticationPrincipal UserDetails userdetails) {


        String uid = userdetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);

        ReturnDTO returnDTO = new ReturnDTO();
        returnDTO.setType(type);
        returnDTO.setContent(content);

        myPageService.returnRequest(returnDTO, itemNo, uploadType, userDTO, file);

        return ResponseEntity.ok().build();
    }

    // 교환하기 모달
    @PostMapping("/my/order/exchange")
    public ResponseEntity<?> exchangeRequest(@RequestParam("type") String type,
                                             @RequestParam("itemNo") Long itemNo,
                                             @RequestParam("content") String content,
                                             @RequestParam("uploadType") String uploadType,
                                             @RequestParam(value = "file", required = false) MultipartFile file,
                                             @AuthenticationPrincipal UserDetails userdetails) {



        String uid = userdetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);

        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setType(type);
        exchangeDTO.setContent(content);


        System.out.println("교환하기 모달 컨트롤러 uploadType: " + uploadType);

        myPageService.exchangeRequest(exchangeDTO, itemNo, uploadType, userDTO, file);

        return ResponseEntity.ok().build();
    }
    
    // 리뷰 모달
    @PostMapping(value = "/my/order/review", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reviewRequest(
            @RequestParam("rating") String ratingStr,
            @RequestParam("content") String content,
            @RequestParam("productId") String productId,
            @RequestParam("uploadType") String uploadType,
            @RequestParam(value = "file1", required = false) MultipartFile file1,
            @RequestParam(value = "file2", required = false) MultipartFile file2,
            @AuthenticationPrincipal UserDetails userDetails) {

        // rating 변환
        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating value");
        }

        // 사용자 정보 조회
        String uid = userDetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);

        // 리뷰 DTO 생성
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setRating(BigDecimal.valueOf(rating));
        reviewDTO.setContent(content);

        // 파일 처리 로직 추가 필요
        myPageService.reviewRegister(reviewDTO, userDTO, productId, uploadType, file1, file2);

        return ResponseEntity.ok().build();
    }



    // 나의 정보 수정
    @PostMapping("/my/info")
    public String modify(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam String email1,
                         @RequestParam String email2,
                         @RequestParam String phonePart1,
                         @RequestParam String phonePart2,
                         @RequestParam String phonePart3,
                         @RequestParam String zip,
                         @RequestParam String addr1,
                         @RequestParam String addr2, Model model){

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);


        // 이메일
        String email = email1 + "@" + email2;
        userDTO.setEmail(email);
        userDTO.setZip(zip);
        userDTO.setAddr1(addr1);
        userDTO.setAddr2(addr2);
        userDTO.setUpdateDate(LocalDateTime.now());


        myPageService.modify(userDTO);


        return "redirect:/my/home";
    }

    @PostMapping("/my/withdraw")
    public String withdraw(@AuthenticationPrincipal UserDetails userDetails){

        String uid = userDetails.getUsername();

        UserDTO user = myPageService.findByUid(uid);

        myPageService.withdraw(user);

        return "redirect:/";
    }

    @GetMapping("/my/info")
    public String info(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        myPageService.splitPhone(userDTO);

        String formattedPhone = myPageService.joinPhone(userDTO);

        userDTO.setHp(formattedPhone);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);


        return "/myPage/mySetting";
    }




}
