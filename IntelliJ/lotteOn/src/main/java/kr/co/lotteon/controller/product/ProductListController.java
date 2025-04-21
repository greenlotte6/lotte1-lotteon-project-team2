package kr.co.lotteon.controller.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductListController {


    // 기본 리스트
    @GetMapping("/product/list")
    public String list() {

        return "/product/list";
    }

    // 판매많은순
    // 낮은가격순
    // 높은가격순
    // 평점높은순
    // 후기많은순
    // 최신등록순

}
