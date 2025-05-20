package kr.co.lotteon.controller.mypage;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lotteon.dto.article.InquiryDTO;
import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
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
import kr.co.lotteon.entity.config.Banner;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.mypage.MyPageService;
import kr.co.lotteon.service.order.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
@Controller
public class MyController {

    private final MyPageService myPageService;
    private final ConfigService configService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/my/home")
    public String myHome(PageRequestDTO pageRequestDTO, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        List<BannerDTO> banners = configService.findBanner("MY1");
        BannerDTO banner = configService.randomBanner(banners);

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

        // 로그인한 유저의 주문 수 조회
        long orderCount = myPageService.orderCount(userDTO);




        // 로그인한 유저의 정보 조회
        myPageService.splitPhone(userDTO);
        String formattedPhone = myPageService.joinPhone(userDTO);
        userDTO.setHp(formattedPhone);

        model.addAttribute("orderCount", orderCount);
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

    @GetMapping("/my/info/check")
    public String myInfoCheck(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        model.addAttribute("userDTO", userDTO);

        return "/myPage/checkPassword";
    }

    @GetMapping("/my/info/check/password")
    @ResponseBody
    public Map<String, Object> checkPassword(@AuthenticationPrincipal UserDetails userDetails) {
        String uid = userDetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);

        Map<String, Object> result = new HashMap<>();
        boolean hasPassword = Objects.nonNull(userDTO.getPass());
        result.put("hasPassword", hasPassword);

        return result;
    }

    @ResponseBody
    @PostMapping("/my/info/check")
    public String passwordCheck(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String inputPassword) {

        String uid = userDetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);

        String encodedPassword = userDTO.getPass();



        if (passwordEncoder.matches(inputPassword, encodedPassword)) {
            return "ok";
        } else {
            return "error";
        }
    }

    @ResponseBody
    @PostMapping("/my/info/change")
    public String passwordUpdate(    @AuthenticationPrincipal UserDetails userDetails,
                                     @RequestParam("newPassword") String newPassword){

        String uid = userDetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);

        myPageService.passwordUpdate(userDTO, newPassword);

        return "success";
    }

    @GetMapping("/my/order")
    public String order(@AuthenticationPrincipal UserDetails userDetails, PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(10);

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        // 로그인한 유저의 주문 수 조회
        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);
        PageResponseDTO<OrderInfoDTO> orderInfoPagingDTO = myPageService.orderInfoPaging(pageRequestDTO, uid);


        model.addAttribute("point", point);
        model.addAttribute("orderInfoPagingDTO", orderInfoPagingDTO);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("orderInfoPagingDTO", orderInfoPagingDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);


        return "/myPage/orderDetails";

    }


    @GetMapping("/my/order/search")
    public String search(@AuthenticationPrincipal UserDetails userDetails,
                         PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(10);

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

        PageResponseDTO<OrderInfoDTO> orderInfoPagingDTO = myPageService.searchOrder(pageRequestDTO, uid);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("orderInfoPagingDTO", orderInfoPagingDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);

        return "/myPage/orderDetailsSearch";

    }

    @ResponseBody
    @GetMapping("/my/order/exist")
    public String orderExist(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam("searchType") String searchType,
                             @RequestParam("start") LocalDate start,
                             @RequestParam("end") LocalDate end,
                             @RequestParam("keyword") String keyword) {


        LocalDateTime localStart = start.atStartOfDay();
        LocalDateTime localEnd = end.atTime(LocalTime.MAX);;

        Boolean exist = myPageService.existOrderByType(localStart, localEnd, searchType, keyword);

        if(exist){
            return "ok";
        }else{
            return "error";
        }

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


        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

        PageResponseDTO<PointDTO> pointDTO = myPageService.pointFindAll(userDTO, pageRequestDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("pointDTO", pointDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);

        return "/myPage/pointDetails";
    }

    @GetMapping ("/my/point/search")
    public String searchPoint(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam(value="startDate", required = false, defaultValue = "") LocalDate startDate,
                              @RequestParam(value="endDate", required = false, defaultValue="") LocalDate endDate,
                              PageRequestDTO pageRequestDTO, Model model){

        String uid = userDetails.getUsername();

        UserDTO userDTO = myPageService.findByUid(uid);

        // 로그인한 유저의 쿠폰 개수 조회
        long getCouponCount = myPageService.getCouponCount(userDTO);

        // 로그인한 유저의 검토중 문의 개수 조회
        long pendingInquiryCount = myPageService.getPendingInquiryCount(userDTO);

        // 로그인한 유저의 포인트 조회
        String point = myPageService.getPoint(userDTO);

        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

        PageResponseDTO<PointDTO> PointDTO = myPageService.searchPoint(userDTO, pageRequestDTO, startDate, endDate);

        model.addAttribute("pointDTO", PointDTO);
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);

        return "/myPage/pointDetailsSearch";
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

        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

        PageResponseDTO<CouponIssueDTO> couponIssueDTO = myPageService.couponIssueFindAll(userDTO, pageRequestDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("couponIssueDTO", couponIssueDTO);
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

        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

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


        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

        PageResponseDTO<InquiryDTO> inquiryDTO = myPageService.inquiryFindAll(userDTO, pageRequestDTO);

        log.info("inquiryDTO : " + inquiryDTO);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("inquiryDTO", inquiryDTO);
        model.addAttribute("getCouponCount", getCouponCount);
        model.addAttribute("pendingInquiryCount", pendingInquiryCount);
        model.addAttribute("point", point);


        return "/myPage/myInquiry";
    }


    // 문의하기 모달
    @PostMapping("/my/order/inquiry")
    public ResponseEntity<?> inquiry(@RequestParam("type") String type,
                        @RequestParam("email") String email,
                        @RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam("prodNo") String prodNo,
                        @AuthenticationPrincipal UserDetails userdetails, InquiryDTO inquiryDTO, HttpServletRequest request){

        String uid = userdetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);


        LocalDate now = LocalDate.now();
        inquiryDTO.setWdate(now);
        inquiryDTO.setRegip(request.getRemoteAddr());
        inquiryDTO.setProdNo(prodNo);
        inquiryDTO.setCateV1(type);
        inquiryDTO.setEmail(email);
        inquiryDTO.setTitle(title);
        inquiryDTO.setContent(content);
        inquiryDTO.setChannel("판매자 게시판");
        inquiryDTO.setCateV2("판매자 게시판");



        myPageService.inquiryRegister(userDTO, inquiryDTO);

        return ResponseEntity.ok().build();
    }
    
    // 구매확정 모달
    @PostMapping("/my/order/confirm")
    public ResponseEntity<?> confirmPurchase(@RequestBody Map<String, Object> payload, PointDTO pointDTO, @AuthenticationPrincipal UserDetails userdetails) {
        Long itemNo = Long.valueOf(payload.get("itemNo").toString());

        String uid = userdetails.getUsername();
        UserDTO userDTO = myPageService.findByUid(uid);
        OrderItemDTO orderItemDTO = myPageService.FindByItemNo(itemNo);

        myPageService.upPoint(userDTO, orderItemDTO);

        boolean result = myPageService.confirmPurchase(itemNo);
        if (result) {
            myPageService.pointRegister(pointDTO, userDTO, orderItemDTO);
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
        myPageService.deleteReviewCache();

        return ResponseEntity.ok().build();
    }



    // 나의 정보 수정
    @PostMapping("/my/info")
    public String modify(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam String email1,
                         @RequestParam String email2,
                         @RequestParam String name,
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
        userDTO.setName(name);
        userDTO.setUpdateDate(LocalDateTime.now());


        myPageService.modify(userDTO);


        return "redirect:/my/info";
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

        long orderCount = myPageService.orderCount(userDTO);
        model.addAttribute("orderCount", orderCount);

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
