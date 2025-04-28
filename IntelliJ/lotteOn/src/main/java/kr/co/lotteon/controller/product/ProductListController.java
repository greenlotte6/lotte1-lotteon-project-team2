
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductListController {

    private final ProductListService productListService;

    // 상품 목록 - 첫 페이지 진입용
    @GetMapping("/product/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO pageResponseDTO = productListService.selectAllForList(pageRequestDTO);
        List<ProductDTO> productList = productListService.selectBestAllForList(pageRequestDTO.getSubCateNo());
        model.addAttribute(pageResponseDTO);
        model.addAttribute(productList);
        log.info("pageResponseDTO:{}", pageResponseDTO);
        return "/product/list/list";
    }


    // 상품 목록 데이터 (Ajax 요청 처리)
    @GetMapping("/product/ajaxList")
    @ResponseBody
    public PageResponseDTO ajaxList(PageRequestDTO pageRequestDTO, Model model,
            @RequestParam(value = "view", defaultValue = "list") String view) {

        log.info("pageRequestDTO:{}", pageRequestDTO);

        PageResponseDTO pageResponseDTO = productListService.sortedProducts(pageRequestDTO);

        pageResponseDTO.setSortType(pageRequestDTO.getSortType());
        pageResponseDTO.setPeriod(pageRequestDTO.getPeriod());
        pageResponseDTO.setView(view);

        log.info("pageResponseDTO:{}", pageResponseDTO);

        return pageResponseDTO;
    }

}