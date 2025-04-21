
package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageResponseDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductController {

    private final ProductService productService;

    // 기본 리스트
    @GetMapping("/product/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productService.selectAllForList(pageRequestDTO);
        List<ProductDTO> productList = productService.selectBestAllForList(pageRequestDTO.getCate());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);


        log.info("pageResponseDTO: {}", pageResponseDTO);
        log.info("productList: {}", productList);
        return "/product/list";
    }

    // 상품 정렬 리스트
    @GetMapping("/product/list/sorted")
    public String getSortedProductList(PageRequestDTO dto, Model model) {
        PageResponseDTO pageResponseDTO = productService.sortedProducts(dto);
        model.addAttribute("pageResponseDTO", pageResponseDTO);
        return "product/list :: productListFragment";
    }

    // 상세보기
    @GetMapping("/product/view")
    public String Productview() {
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