package kr.co.lotteon.controller.cart;

import kr.co.lotteon.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CartController {

    private final CartService cartService;


//    // 장바구니 수량 변경 시 Redis에 저장
//    @PostMapping("/cart/update")
//    public ResponseEntity<?> updateQuantity(@RequestParam("cartNo") List<Integer> cartNos,
//                                            @RequestParam("cartProdCount") List<Integer> cartProdCounts) {
//
//        for (int i = 0; i < cartNos.size(); i++) {
//            Integer cartNo = cartNos.get(i);
//            Integer cartProdCount = cartProdCounts.get(i);
//
//            if (cartProdCount <= 0) {
//                continue;
//            }
//
//            cartService.updateQuantityInRedis(cartNo, cartProdCount);
//        }
//
//        return ResponseEntity.ok("장바구니 수량이 업데이트되었습니다.");
//    }





}
