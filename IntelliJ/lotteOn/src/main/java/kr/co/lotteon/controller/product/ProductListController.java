
package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.service.product.ProductListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductListController {

    private final ProductListService productListService;

    // 기본 리스트
    @GetMapping("/product/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.selectAllForList(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/list";
    }

    // 상품 정렬 리스트 //

    // 낮은가격순
    @GetMapping("/product/list/lowPrice")
    public String lowPrice(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/lowPrice";
    }

    // 높은가격순
    @GetMapping("/product/list/highPrice")
    public String highPrice(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/highPrice";
    }

    // 평점높은순
    @GetMapping("/product/list/highRating")
    public String highRating(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/highRating";
    }

    // 최근등록순
    @GetMapping("/product/list/latest")
    public String latest(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/latest";
    }

    // 판매많은순 1year
    @GetMapping("/product/list/mostSales1year")
    public String mostSales1year(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/mostSales1year";
    }

    // 판매많은순 6month
    @GetMapping("/product/list/mostSales6month")
    public String mostSales6month(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/mostSales6month";
    }

    // 판매많은순 1month
    @GetMapping("/product/list/mostSales1month")
    public String mostSales1month(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/mostSales1month";
    }

    // 후기많은순 1year
    @GetMapping("/product/list/manyReviews1year")
    public String manyReviews1year(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/manyReviews1year";
    }

    // 후기많은순 6month
    @GetMapping("/product/list/manyReviews6month")
    public String manyReviews6month(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        return "/product/list/manyReviews6month";
    }

    // 후기많은순 1month
    @GetMapping("/product/list/manyReviews1month")
    public String manyReviews1month(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);

        log.info("pageRequestDTO:{}", pageRequestDTO);
        return "/product/list/manyReviews1month";
    }




}