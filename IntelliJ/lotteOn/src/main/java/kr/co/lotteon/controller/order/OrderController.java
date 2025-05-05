package kr.co.lotteon.controller.order;


import kr.co.lotteon.dto.order.OrderItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class OrderController {

    @PostMapping("/order/submit")
    public String orderSubmit(OrderItemDTO orderItemDTO, @RequestParam String point, @RequestParam String cartNo) {

        log.info("orderItemDTO = {}", orderItemDTO);
        log.info("point = {}", point);
    //OrderDTO orderDTO, CouponIssueDTO couponIssueDTO, PointDTO pointDTO, UserDetailsDTO userDetailsDTO, OrderItemDTO orderItemDTO, CartDTO cartDTO, DeliveryDTO deliveryDTO

        return null;
    }

}
