
package kr.co.lotteon.controller.product;

import jakarta.mail.FetchProfile;
import kr.co.lotteon.dao.ProductMapper;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.coupon.CouponIssueDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.service.article.InquiryService;
import kr.co.lotteon.service.cart.CartService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.feedback.ReviewService;
import kr.co.lotteon.service.product.ProductDetailService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final ProductService productService;
    private final CouponService couponService;
    private final CartService cartService;

    // 상품 보기 - 첫 페이지 진입용
    @GetMapping("/product/view")
    public String view(PageRequestDTO pageRequestDTO, Model model) {
        pageRequestDTO.setSize(5);
        String prodNo = pageRequestDTO.getProdNo();
        // 상품 + 상품이미지
        ProductDTO productDTO = productMapper.selectProductByProdNo(prodNo);
        // 상품 옵션 Split
        productDTO = productService.OptionSplit(productDTO);
        // 상품 상세
        ProductDetailDTO productDetailDTO = productDetailService.findByProdNo(pageRequestDTO);
        // 리뷰
        PageResponseDTO reviewPageResponseDTO = reviewService.selectAllForList(pageRequestDTO);
        // qna
        PageResponseDTO inquiryPageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);

        model.addAttribute(productDTO);
        model.addAttribute(productDetailDTO);
        model.addAttribute("reviewPageResponseDTO", reviewPageResponseDTO);
        model.addAttribute("inquiryPageResponseDTO", inquiryPageResponseDTO);

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


    // 장바구니
    @PostMapping("/product/cart")
    @ResponseBody
    public int cart(@RequestBody ItemRequestDTO itemRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {

        log.info("itemRequestDTO=" + itemRequestDTO);
        log.info("userDetails=" + userDetails);
        int result = cartService.addToCart(itemRequestDTO, userDetails);
        log.info("result: " + result);

        return result;
    }


    // 주문하기
    @GetMapping("/product/order")
    public String order() {
        return "/product/order";
    }

    //주문완료
    @GetMapping("/product/complete")
    public String complete() {
        return "/product/order_completed";
    }

    @GetMapping("/product/search")
    public String search() {
        return "/product/search";
    }


}