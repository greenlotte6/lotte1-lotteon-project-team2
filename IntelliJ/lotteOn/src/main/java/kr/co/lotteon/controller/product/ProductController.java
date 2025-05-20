package kr.co.lotteon.controller.product;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dao.ProductMapper;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.entity.config.Banner;
import kr.co.lotteon.service.article.InquiryService;
import kr.co.lotteon.service.cart.CartService;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.feedback.ReviewService;
import kr.co.lotteon.service.order.OrderService;
import kr.co.lotteon.service.product.ProductDetailService;
import kr.co.lotteon.service.product.ProductService;
import kr.co.lotteon.service.user.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final ProductService productService;
    private final CouponService couponService;
    private final CartService cartService;
    private final ConfigService configService;
    private final OrderService orderService;
    private final UserDetailsService userDetailsService;

    // 상품 보기 - 첫 페이지 진입용
    @GetMapping("/product/view")
    public String view(PageRequestDTO pageRequestDTO, Model model) {

        List<BannerDTO> banners= configService.findBanner("PRODUCT1");
        BannerDTO banner = configService.randomBanner(banners);

        model.addAttribute("banner", banner);

        pageRequestDTO.setSize(5);
        String prodNo = pageRequestDTO.getProdNo();

        // 상품 조회수 추가
        productService.hitCountUp(prodNo);
        // 상품
        ProductDTO productDTO = productService.selectProductByProdNo(prodNo);
        // 상품 옵션 Split
        productDTO = productService.OptionSplit(productDTO);
        // 상품 상세
        ProductDetailDTO productDetailDTO = productDetailService.findByProdNo(pageRequestDTO);
        // 리뷰
        PageResponseDTO reviewPageResponseDTO = reviewService.selectAllForList(pageRequestDTO);
        // qna
        PageResponseDTO inquiryPageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);
        // 상품 이미지
        ProductImageDTO productImageDTO = productService.findImageByNo(productDTO);

        model.addAttribute(productDTO);
        model.addAttribute(productDetailDTO);
        model.addAttribute("productImage", productImageDTO);
        model.addAttribute("reviewPageResponseDTO", reviewPageResponseDTO);
        model.addAttribute("inquiryPageResponseDTO", inquiryPageResponseDTO);

        log.info("productDTO: {}", productDTO);

        return "/product/view/view";
    }

    // 리뷰 목록 데이터 (Ajax 요청 처리)
    @GetMapping("/product/reviewList")
    @ResponseBody
    public PageResponseDTO getReviews(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(5);
        PageResponseDTO pageResponseDTO = reviewService.selectAllForList(pageRequestDTO);
        return pageResponseDTO;
    }

    // 문의 Ajax
    @GetMapping("/product/qnaList")
    @ResponseBody
    public PageResponseDTO getQna(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(5);
        PageResponseDTO pageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);
        return pageResponseDTO;
    }


    // 쿠폰
    @GetMapping("/product/coupon")
    @ResponseBody
    public List<CouponDTO> coupon(@RequestParam String company) {
        List<CouponDTO> couponDTOList = couponService.findAllByCompany(company);
        return couponDTOList;
    }


    // 쿠폰 - 인가 처리 리다이렉트
    @GetMapping("/product/ViewLoginCheck")
    public String viewLoginCheck(@RequestParam("prodNo") String prodNo) {
        return "redirect:/product/view?prodNo=" + prodNo;
    }


    // 쿠폰 발급받기
    @PostMapping("/product/couponIssue")
    @ResponseBody
    public ResponseEntity<String> couponIssue(@RequestBody CouponIssueDTO couponIssueDTO,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        int result = couponService.couponIssue(couponIssueDTO, userDetails);
        if (result == 1) {
            return ResponseEntity.ok("쿠폰이 발급되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("쿠폰 발급 중 오류가 발생했습니다.");
        }
    }


    // 장바구니 상품 담기
    @PostMapping("/product/addCart")
    @ResponseBody
    public int cart(@RequestBody ItemRequestDTO itemRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        int result = cartService.addToCart(itemRequestDTO, userDetails);
        return result;
    }


    // 장바구니 View
    @GetMapping("/product/cart")
    public String cart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<CartDTO> cartDTOList = cartService.findAllByUid(userDetails);
        model.addAttribute(cartDTOList);
        return "/product/cart";
    }


    // 장바구니 상품 삭제
    @GetMapping("/product/removeItem")
    @ResponseBody
    public int removeItem(int cartNo){
        int result = cartService.deleteByCartNo(cartNo);
        return result;
    }


    // 장바구니 수량 업데이트
    @PostMapping("/update/cartProdCount")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestParam Integer cartNo, @RequestParam int newQuantity) {
        cartService.updateCartProdCount(cartNo, newQuantity);
        return ResponseEntity.ok("수량이 성공적으로 업데이트되었습니다.");
    }



    // 장바구니 담고 주문하기 View
    @GetMapping("/product/order")
    public String order(@RequestParam("cartNo") List<Integer> cartNos,
                        Model model) {

        List<CartDTO> cartDTOList = new ArrayList<>();

        for (int i = 0; i < cartNos.size(); i++) {
            Integer cartNo = cartNos.get(i);

            CartDTO cartDTO = orderService.findByCartNo(cartNo);

            cartDTOList.add(cartDTO);
        }

        String uid = cartDTOList.get(0).getUser().getUid();

        UserDetailsDTO userDetailsDTO = userDetailsService.findByUser(uid);
        List<CouponIssueDTO> couponIssueDTOList = couponService.findAllByUser(uid);

        model.addAttribute(cartDTOList);
        model.addAttribute(couponIssueDTOList);
        model.addAttribute(userDetailsDTO);

        return  "/product/order/order";
    }

    // 바로 주문하기 View
    @PostMapping("/product/order/direct")
    public String order(@RequestParam Map<String, String> options,
                        ItemRequestDTO itemRequestDTO,
                        @AuthenticationPrincipal UserDetails userDetails,
                        HttpSession session,
                        Model model){

        List<CartDTO> cartDTOList = orderService.makeCart(itemRequestDTO, userDetails, options);

        String uid = userDetails.getUsername();
        UserDetailsDTO userDetailsDTO = userDetailsService.findByUser(uid);
        List<CouponIssueDTO> couponIssueDTOList = couponService.findAllByUser(uid);

        model.addAttribute(cartDTOList);
        model.addAttribute(couponIssueDTOList);
        model.addAttribute(userDetailsDTO);

        session.setAttribute("cartDTO", cartDTOList.get(0));

        return "/product/order/order";
    }

}