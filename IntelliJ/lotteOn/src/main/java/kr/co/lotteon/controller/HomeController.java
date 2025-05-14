package kr.co.lotteon.controller;

import kr.co.lotteon.dto.config.BannerDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.entity.config.Banner;
import kr.co.lotteon.service.config.ConfigService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {

    private final ConfigService configService;
    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model){
        List<BannerDTO> banners = configService.findBanner("MAIN1");
        BannerDTO banner = configService.randomBanner(banners);

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

        //리뷰 많은 상품 8개
        List<ProductDTO> reviewManyList = productService.findReviewManyTop8();

        model.addAttribute("hitList", hitList);
        model.addAttribute("bestList", bestList);

        return "/index";
    }

    @GetMapping("/main/discount")
    @ResponseBody
    public List<ProductDTO> loadMoreDiscountProducts() {
        // productService.deleteDiscountCache();

        delay(); // 딜레이
        List<ProductDTO> list =productService.findDiscountTop8();
        return list;
    }

    //평점 높은 상품 8개 (추천상품)
    @GetMapping("/main/recommendation")
    @ResponseBody
    public List<ProductDTO> loadMoreRecommendProducts() {
        log.info("추천 상품 출력: ");
        //productService.deleteRecommendationCache();
        delay(); // 딜레이
        List<ProductDTO> list =productService.findReviewTop8();
        return list;
    }

    @GetMapping("/main/recent")
    @ResponseBody
    public List<ProductDTO> loadMoreRecentProducts() {
        log.info("최신 상품 출력: ");
        //productService.deleteRecentCache();
        delay(); // 딜레이
        List<ProductDTO> list = productService.findReviewTop8();
        return list;
    }

    // 리뷰 많은 순서
    @GetMapping("/main/review/many")
    @ResponseBody
    public List<ProductDTO> loadMoreReviewProducts() {
        log.info("리뷰 많은 상품 출력: ");
        // productService.deleteReviewManyCache();
        delay(); // 딜레이
        List<ProductDTO> list = productService.findReviewManyTop8();
        return list;
    }

    public void delay(){

        try {
            Thread.sleep(600); // 0.6초 지연
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
