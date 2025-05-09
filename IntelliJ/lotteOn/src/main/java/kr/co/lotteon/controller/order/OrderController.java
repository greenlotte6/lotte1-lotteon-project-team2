package kr.co.lotteon.controller.order;


import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.service.cart.CartService;
import kr.co.lotteon.service.coupon.CouponService;
import kr.co.lotteon.service.kakao.KakaoPayService;
import kr.co.lotteon.service.order.OrderItemService;
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

import java.util.List;

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
    private final OrderItemService orderItemService;

    /*
    * 1. 주문
    * Order 테이블에 데이터 넣기
    * OrderItem 테이블에 데이터 넣기
    * */

    /*
    * 2. 포인트
    * Point 테이블에 사용한 포인트량, 적립된 포인트량 기록
    * UserDetail 테이블에 포인트 총량 기록
    * */

    /*
    * 3. 상품
    * Product 테이블에 재고(prodStock), 판매량(prodSold) 계산하기
    * */
    
    /*
    * 4. 쿠폰
    * 사용한 쿠폰 상태 바꾸기
    * */
    
    /*
    * 4. 장바구니
    * 구매한 장바구니 cartNo 지우기
    * */


    /*
    * 진행한거 :
    * 1. 장바구니 페이지 체크 안됬을 때 생기는 에러 해결
    * 2. 결제 페이지 버퍼링 추가
    * 3. Order 테이블 데이터 넣기
    * 4. OrderItem 테이블 데이터 넣기
    * 5. 상품 View 조회 시 조회수 + 1
    * 6. 장바구니 지우기
    *    추가 사항: orderItem에 카테고리 추가햇어요.
    *    관리자 페이지에서 카테고리 별로 매출 계산해야되서
    *
    * */

    @PostMapping("/order/submit")
    public ResponseEntity orderSubmit(Amount amount,
                              OrderDTO orderDTO,
                              HttpSession session,
                              @RequestParam int usedPoint,
                              @RequestParam int earnedPoint,
                              @RequestParam(value = "cartNo", required = false) List<Integer> cartNos,
                              @RequestParam String receiverAddr1,
                              @RequestParam String receiverAddr2,
                              @RequestParam(value = "issueNo", required = false) long issueNo,
                              @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        log.info("orderDTO: {}", orderDTO);


        orderDTO.setTotalQuantity(amount.getQuantity());
        orderDTO.setUid(userDetails.getUsername());
        orderDTO.setOrderAddr(receiverAddr1 + " " + receiverAddr2);

        // Order 테이블 등록하기 -> 등록 후 orderItem을 등록하기 위한 order 출력
        int orderNo = orderService.registerOrder(orderDTO);

        if(cartNos != null) {

            System.out.println("장바구니 로직");
            // 상세 주문 등록하기
            orderService.registerOrderItem(orderNo, cartNos);

            // 상품 재고, 판매량 계산
            productService.changeSoldAndStock(cartNos);

            // 장바구니 지우기
            // cartService.deleteAllByCartNo(cartNos);
        }else{

            System.out.println("바로구매 로직");
            
            CartDTO cartDTO = (CartDTO) session.getAttribute("cartDTO");
            
            System.out.println("상품 저장");
            orderService.directOrderItem(orderNo, cartDTO);

            // 상품 재고, 판매량 계산
            productService.directSoldAndStock(cartDTO);
        }

        // 쿠폰 사용상태로 바꾸기
        // couponService.changeState(issueNo);

        // 포인트 사용 시 기록
        pointService.changePoint(usedPoint, earnedPoint, userDetails);

        session.setAttribute("orderNo", orderNo);

        amount = orderService.getAmount(orderNo, userDetails, orderDTO);

        // 카카오페이 결제 요청
        return kakaoPayService.kakaoPayReady(amount);
    }

    // 결제 성공
    @GetMapping("/payment/success")
    public String afterPayRequest(@RequestParam("pg_token") String pgToken, HttpSession session) {

        Integer orderNo = (Integer) session.getAttribute("orderNo");
        OrderDTO orderDTO = orderService.findAllByOrderNo(orderNo);
        session.setAttribute("orderDTO", orderDTO);

        kakaoPayService.approveResponse(pgToken);

        return "redirect:/product/order_completed";
    }

    @GetMapping("/product/order_completed")
    public String orderCompleted(HttpSession session, Model model) {

        OrderDTO orderDTO = (OrderDTO) session.getAttribute("orderDTO");
        model.addAttribute("orderDTO", orderDTO);

        return "/product/order/order_completed";
    }




}
