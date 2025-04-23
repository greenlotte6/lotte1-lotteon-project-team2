
package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import kr.co.lotteon.service.article.InquiryService;
import kr.co.lotteon.service.product.ProductDetailService;
import kr.co.lotteon.service.product.ProductService;
import kr.co.lotteon.service.feedback.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;
    private final InquiryService inquiryService;

    // 상세보기
    @GetMapping("/product/view")
    public String Productview(PageRequestDTO pageRequestDTO, Model model) {
        productService.increaseHit(pageRequestDTO);
        // 상품
        ProductDTO product = productService.findByProdNo(pageRequestDTO);
        // 상품 상세
        ProductDetailDTO productDetailDTO = productDetailService.findByProdNo(pageRequestDTO);
        // 리뷰
        PageResponseDTO reviewPageResponseDTO = reviewService.selectAllForList(pageRequestDTO);
        // qna
        PageResponseDTO inquiryPageResponseDTO = inquiryService.selectAllForList(pageRequestDTO);



        return "/product/beauty/perfume/diptyque_fleurdepeau";
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