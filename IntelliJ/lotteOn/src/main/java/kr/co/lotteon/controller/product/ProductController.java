
package kr.co.lotteon.controller.product;

import kr.co.lotteon.dao.ProductMapper;
import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.service.article.InquiryService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.feedback.ReviewService;
import kr.co.lotteon.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductMapper productMapper;
    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;
    private final CouponService couponService;

    // 상품 보기 - 첫 페이지 진입용
    @GetMapping("/product/view")
    public String view(PageRequestDTO pageRequestDTO, Model model) {
        pageRequestDTO.setSize(5);
        String prodNo = pageRequestDTO.getProdNo();
        // 상품 + 상품이미지
        ProductDTO productDTO = productMapper.selectProductByProdNo(prodNo);
        // 쿠폰
        List<CouponDTO> couponDTOList = couponService.findAllByCompany(productDTO.getCompany());
        // 상품 상세
        ProductDetailDTO productDetailDTO = productDetailService.findByProdNo(pageRequestDTO);
        // 리뷰
        PageResponseDTO reviewPageResponseDTO = reviewService.selectAllForList(pageRequestDTO);
        // qna
        PageResponseDTO inquiryPageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);
        model.addAttribute(productDTO);
        model.addAttribute(couponDTOList);
        model.addAttribute(productDetailDTO);
        model.addAttribute("reviewPageResponseDTO", reviewPageResponseDTO);
        model.addAttribute("inquiryPageResponseDTO", inquiryPageResponseDTO);

        log.info("inquiryPageResponseDTO: {}", inquiryPageResponseDTO);

        return "/product/view/view";
    }

    // 리뷰 목록 데이터 (Ajax 요청 처리)
    @GetMapping("/product/reviewList")
    @ResponseBody
    public PageResponseDTO getReviews(
            @RequestParam(defaultValue = "1") int pg,
            @RequestParam String prodNo,
            @RequestParam(value = "sortType", required = false) String sortType) { // sortType 파라미터 사용

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPg(pg);
        pageRequestDTO.setProdNo(prodNo);
        pageRequestDTO.setSize(5);
        pageRequestDTO.setSortType(sortType); // sortType 설정

        PageResponseDTO pageResponseDTO = reviewService.selectAllForList(pageRequestDTO);

        log.info("pageRequestDTO:{}", pageRequestDTO);
        log.info("pageResponseDTO:{}", pageResponseDTO);

        return pageResponseDTO;
    }

    // 문의 Ajax

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