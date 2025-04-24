
package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.dto.page.PageViewDTO;
import kr.co.lotteon.service.product.ProductService;
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

    // 상세보기
    @GetMapping("/product/view")
    public String productView(PageRequestDTO pageRequestDTO, Model model) {
        productService.increaseHit(pageRequestDTO);
        PageViewDTO pageViewDTO = productService.getPageViewDTO(pageRequestDTO);
        model.addAttribute("pageView", pageViewDTO);
        return "/product/beauty/perfume/perfumeView";
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