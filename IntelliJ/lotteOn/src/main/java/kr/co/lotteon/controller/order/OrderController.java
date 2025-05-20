package kr.co.lotteon.controller.order;


import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.dto.order.OrderItemDTO;
import kr.co.lotteon.dto.user.UserDetailsDTO;
import kr.co.lotteon.service.cart.CartService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.kakao.KakaoPayService;
import kr.co.lotteon.service.order.OrderService;
import kr.co.lotteon.service.point.PointService;
import kr.co.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final CouponService couponService;
    private final PointService pointService;
    private final ProductService productService;
    private final KakaoPayService kakaoPayService;

    @PostMapping("/order/submit")
    public ResponseEntity orderSubmit(Amount amount,
                              OrderDTO orderDTO,
                              HttpSession session,
                              @RequestParam(value = "itemPoint", required = false) List<Integer> itemPointList,
                              @RequestParam(value = "usedPoint", required = false) Integer usedPoint,
                              @RequestParam(value = "cartNo", required = false) List<Integer> cartNos,
                              @RequestParam(value = "receiverAddr1", required = false) String receiverAddr1,
                              @RequestParam(value = "receiverAddr2", required = false) String receiverAddr2,
                              @RequestParam(value = "issueNo", required = false) Long issueNo,
                              @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        orderDTO.setTotalQuantity(amount.getQuantity());
        orderDTO.setUid(userDetails.getUsername());
        orderDTO.setOrderAddr(receiverAddr1 + " " + receiverAddr2);

        // Order 테이블 등록하기 -> 등록 후 orderItem을 등록하기 위한 order 출력
        int orderNo = orderService.registerOrder(orderDTO);

        if(cartNos != null) {

            // 상세 주문 등록하기
            orderService.registerOrderItem(orderNo, cartNos, itemPointList);

            // 상품 재고, 판매량 계산
            productService.changeSoldAndStock(cartNos);

            // 장바구니 지우기
            cartService.deleteAllByCartNo(cartNos);

        }else{

            // 세션에서 cartDTO 꺼내기
            CartDTO cartDTO = (CartDTO) session.getAttribute("cartDTO");

            // 상세 주문 등록하기
            orderService.directOrderItem(orderNo, cartDTO, itemPointList);

            // 상품 재고, 판매량 계산
            productService.directSoldAndStock(cartDTO);
        }

        // 쿠폰 사용상태로 바꾸기
        couponService.changeState(issueNo);

        // 포인트 사용 시 기록
        UserDetailsDTO userDeatilsDTO = pointService.changePoint(usedPoint, userDetails, orderNo);


        session.setAttribute("orderNo", orderNo);

        amount = orderService.getAmount(orderNo, userDetails, orderDTO);

        Map<String, Object> response = new HashMap<>();

        // 카카오페이 결제 요청
        if ("카카오페이".equals(orderDTO.getPayment())) {
            return kakaoPayService.kakaoPayReady(amount);
        } else {
            OrderDTO order = orderService.findOrder(orderNo);
            List<OrderItemDTO> orderList = orderService.findOrderItems(order);
            order.setOrderItems(orderList);

            session.setAttribute("orderDTO", order);

            response.put("type", "일반");
            return ResponseEntity.ok(response);
        }
    }

    // 결제 성공
    @GetMapping("/payment/success")
    public String afterPayRequest(@RequestParam(value="pg_token", required = false) String pgToken, HttpSession session) {

        Integer orderNo = (Integer) session.getAttribute("orderNo");
        OrderDTO orderDTO = orderService.findAllByOrderNo(orderNo);

        session.setAttribute("orderDTO", orderDTO);

        if(pgToken != null) {
            kakaoPayService.approveResponse(pgToken);
        }
        return "redirect:/product/order_completed";
    }

    @GetMapping("/product/order_completed")
    public String orderCompleted(HttpSession session, Model model) {

        OrderDTO orderDTO = (OrderDTO) session.getAttribute("orderDTO");

        model.addAttribute("orderDTO", orderDTO);

        return "/product/order/order_completed";
    }




}
