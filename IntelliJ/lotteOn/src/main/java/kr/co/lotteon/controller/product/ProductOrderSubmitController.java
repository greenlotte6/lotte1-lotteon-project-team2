package kr.co.lotteon.controller.product;

import jakarta.servlet.http.HttpSession;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.kakao.KakaoApproveResponse;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.service.kakao.KakaoPayService;
import kr.co.lotteon.service.product.OrderTransactionService;
import kr.co.lotteon.service.product.ProductOrderSubmitService;
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
public class ProductOrderSubmitController {

    private final KakaoPayService kakaoPayService;
    private final ProductOrderSubmitService productOrderSubmitService;
    private final OrderTransactionService orderTransactionService;

    @PostMapping("/order/submit")
    public ResponseEntity orderSubmit(Amount amount,
                                      OrderDTO orderDTO,
                                      HttpSession session,
                                      @RequestParam(value = "cartNo", required = false) List<Integer> cartNos,
                                      @RequestParam(value = "usedPoint", required = false) Integer usedPoint,
                                      @RequestParam(value = "issueNo", required = false) Long issueNo,
                                      @RequestParam(value = "itemPoint", required = false) List<Integer> itemPointList,
                                      @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        return orderTransactionService.processOrderAndPay(amount, orderDTO, session, cartNos, usedPoint, issueNo, itemPointList, userDetails);
    }


    // 결제 성공
    @GetMapping("/payment/success")
    public String afterPayRequest(@RequestParam(value="pg_token", required = false) String pgToken, HttpSession session) {
        if (pgToken != null) {
            try {
                KakaoApproveResponse approveResponse = kakaoPayService.approveResponse(pgToken);

                if (approveResponse != null) {
                    Integer orderNo = (Integer) session.getAttribute("orderNo");
                    productOrderSubmitService.updateOrderStatusToCompleted(orderNo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/product/order_completed";
    }


    // 주문 완료 View
    @GetMapping("/product/order_completed")
    public String orderCompleted(HttpSession session, Model model) {
        Integer orderNo = (Integer) session.getAttribute("orderNo");
        OrderDTO orderDTO = productOrderSubmitService.findAllByOrderNo(orderNo);

        model.addAttribute("orderDTO", orderDTO);

        return "/product/order/order_completed";
    }
}
