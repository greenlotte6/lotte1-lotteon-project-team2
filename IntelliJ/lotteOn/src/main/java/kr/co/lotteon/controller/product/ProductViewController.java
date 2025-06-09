package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.service.product.ProductViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductViewController {

    private final ProductViewService productViewService;


    // 상품 보기 - 첫 페이지 진입
    @GetMapping("/product/view")
    public String view(PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setSize(5);
        String prodNo = pageRequestDTO.getProdNo();

        // 상품, 판매자(사용자), 메인 카테고리, 서브카테고리, 상품이미지, 상품상세 정보
        ProductDTO productDTO = productViewService.selectProductByProdNo(prodNo);
        // 광고 정보
        List<BannerDTO> banners= productViewService.findBanner("PRODUCT1");
        BannerDTO banner = productViewService.randomBanner(banners);
        // 리뷰 정보
        PageResponseDTO reviewPageResponseDTO = productViewService.selectAllForReview(pageRequestDTO);
        // 문의 정보
        PageResponseDTO inquiryPageResponseDTO = productViewService.selectAllForQna(pageRequestDTO);
        // 상품 조회수 추가
        if(productDTO != null) {
            productViewService.hitCountUp(prodNo);
        }

        model.addAttribute(productDTO);
        model.addAttribute("banner", banner);
        model.addAttribute("reviewPageResponseDTO", reviewPageResponseDTO);
        model.addAttribute("inquiryPageResponseDTO", inquiryPageResponseDTO);

        return "/product/view/view";
    }


    // 리뷰 Ajax 요청 처리
    @GetMapping("/product/reviewList")
    @ResponseBody
    public PageResponseDTO getReviews(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(5);
        PageResponseDTO pageResponseDTO = productViewService.selectAllForReview(pageRequestDTO);
        return pageResponseDTO;
    }


    // 문의 Ajax
    @GetMapping("/product/qnaList")
    @ResponseBody
    public PageResponseDTO getQna(PageRequestDTO pageRequestDTO) {
        pageRequestDTO.setSize(5);
        PageResponseDTO pageResponseDTO = productViewService.selectAllForQna(pageRequestDTO);
        return pageResponseDTO;
    }


}