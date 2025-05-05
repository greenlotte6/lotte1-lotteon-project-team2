package kr.co.lotteon.controller.order;


import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    /*
    * 1. 주문
    * Order 테이블에 데이터 넣기
    * OrderDetail 테이블에 데이터 넣기
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
    * 장바구니 페이지 체크 안됬을 때 생기는 에러 해결
    * 결제 페이지 버퍼링 추가
    * Order 테이블 데이터 넣기
    * OrderItem 테이블 데이터 넣기 
    * */

    @PostMapping("/order/submit")
    public String orderSubmit(OrderDTO orderDTO,
                              @RequestParam int point,
                              @RequestParam("cartNo") List<Integer> cartNos,
                              @RequestParam String receiverAddr1,
                              @RequestParam String receiverAddr2,
                              @RequestParam(value = "couponNo", required = false) String couponNo,
                              @AuthenticationPrincipal UserDetails userDetails) {

        orderDTO.setUid(userDetails.getUsername());
        orderDTO.setOrderAddr(receiverAddr1 + " " + receiverAddr2);

        /*
        * orderStatus, orderDate -> 둘 다 자동등록 따로 설정 x
        * orderSender=null, senderHp=null -> User 조회해서 이름, hp 넣어줄 예정
        * */

        // Order 테이블 등록하기 -> 등록 후 orderItem을 등록하기 위한 order 출력
        int orderNo = orderService.registerOrder(orderDTO);

        // 상세 주문 등록하기
        orderService.registerOrderItem(orderNo, cartNos);
        
        log.info("orderDTO = {}", orderDTO);
        log.info("point = {}", point);
        log.info("cartNo = {}", cartNos);
        log.info("couponNo = {}", couponNo);
        log.info("recipientAddr1 = {}", receiverAddr1);
        log.info("recipientAddr2 = {}", receiverAddr2);


        return "redirect:/";
    }

}
