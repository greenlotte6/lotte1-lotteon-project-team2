package kr.co.lotteon.controller.order;


import kr.co.lotteon.dto.order.OrderDTO;
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
    public String orderSubmit(OrderDTO orderDTO,
                              @RequestParam int point,
                              @RequestParam int cartNo,
                              @RequestParam String receiverAddr1,
                              @RequestParam String receiverAddr2,
                              @RequestParam(value = "couponNo", required = false) String couponNo) {

        log.info("orderDTO = {}", orderDTO);
        log.info("point = {}", point);
        log.info("cartNo = {}", cartNo);
        log.info("couponNo = {}", couponNo);
        log.info("recipientAddr1 = {}", receiverAddr1);
        log.info("recipientAddr2 = {}", receiverAddr2);


        return null;
    }

}
