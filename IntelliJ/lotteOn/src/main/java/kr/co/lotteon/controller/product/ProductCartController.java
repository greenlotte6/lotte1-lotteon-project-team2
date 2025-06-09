package kr.co.lotteon.controller.product;

import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.page.ItemRequestDTO;
import kr.co.lotteon.service.product.ProductCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class ProductCartController {

    private final ProductCartService productCartService;


    // 장바구니 상품 담기
    @PostMapping("/product/addCart")
    @ResponseBody
    public int cart(@RequestBody ItemRequestDTO itemRequestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        int result = productCartService.addToCart(itemRequestDTO, userDetails);
        return result;
    }


    // 장바구니 View
    @GetMapping("/product/cart")
    public String cart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<CartDTO> cartDTOList = productCartService.findAllByUid(userDetails);
        model.addAttribute(cartDTOList);
        return "/product/cart";
    }


    // 장바구니 상품 삭제
    @GetMapping("/product/removeItem")
    @ResponseBody
    public int removeItem(int cartNo){
        int result = productCartService.deleteByCartNo(cartNo);
        return result;
    }


    // 장바구니 수량 업데이트
    @PostMapping("/update/cartProdCount")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestParam Integer cartNo, @RequestParam int newQuantity) {
        productCartService.updateCartProdCount(cartNo, newQuantity);
        return ResponseEntity.ok("수량이 성공적으로 업데이트되었습니다.");
    }

}
