package kr.co.lotteon.controller;

import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {

    private final ConfigService configService;
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model){
        BannerDTO banner = configService.findBanner("MAIN1");
        List<BannerDTO> bannerDTOS = configService.findBannerByCate("MAIN2");

        model.addAttribute("banner", banner);
        model.addAttribute("bannerDTOS", bannerDTOS);

        // 베스트, 히트 , 추천 , 최신, 할인,

        //베스트 상품 5개 / 판매량 높은순
        List<ProductDTO> bestList = productService.findBestTop5();

        //조회수 많은 상품 8개
        List<ProductDTO> hitList = productService.findHitTop8();

        //평점 높은 상품 8개 (추천상품)
        List<ProductDTO> reviewTopList = productService.findReviewTop8();

        //최신 상품
        List<ProductDTO> recentList = productService.findRecentTop8();

        //할인 높은 상품 8개
        List<ProductDTO> discountList = productService.findDiscountTop8();

        //리뷰 많은 상품 8개
        List<ProductDTO> reviewManyList = productService.findReviewManyTop8();


        model.addAttribute("discountList", discountList);
        model.addAttribute("hitList", hitList);
        model.addAttribute("reviewTopList" , reviewTopList);
        model.addAttribute("reviewManyList", reviewManyList);
        model.addAttribute("bestList", bestList);
        model.addAttribute("recentList", recentList);

        return "/index";
    }

}
