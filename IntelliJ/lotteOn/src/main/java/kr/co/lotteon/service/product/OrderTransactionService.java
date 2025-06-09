package kr.co.lotteon.service.product;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import kr.co.lotteon.dto.cart.CartDTO;
import kr.co.lotteon.dto.kakao.Amount;
import kr.co.lotteon.dto.order.OrderDTO;
import kr.co.lotteon.service.kakao.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderTransactionService {

    private final ProductOrderSubmitService productOrderSubmitService;
    private final KakaoPayService kakaoPayService;

    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> processOrderAndPay(Amount amount,
                                                OrderDTO orderDTO,
                                                HttpSession session,
                                                List<Integer> cartNos,
                                                Integer usedPoint,
                                                Long issueNo,
                                                List<Integer> itemPointList,
                                                UserDetails userDetails) throws Exception {

        // 1. 주문 데이터 준비
        productOrderSubmitService.prepareOrderDTO(orderDTO, amount, userDetails);

        // 2. 주문 저장
        int orderNo = productOrderSubmitService.registerOrder(orderDTO);

        // 3. 상세 주문 저장
        productOrderSubmitService.registerOrderItem(orderNo, cartNos, itemPointList, session);

        // 4. 바로 주문 처리
        if (cartNos == null) {
            CartDTO cartDTO = (CartDTO) session.getAttribute("cartDTO");
            cartNos = Collections.singletonList(cartDTO.getCartNo());
        }

        // 5. 재고, 판매량 갱신
        productOrderSubmitService.changeSoldAndStock(cartNos);

        // 6. 쿠폰 사용 처리
        productOrderSubmitService.changeState(issueNo);

        // 7. 포인트 차감
        productOrderSubmitService.changePoint(usedPoint, userDetails, orderNo);

        // 8. 장바구니 비우기
        productOrderSubmitService.deleteAllByCartNo(cartNos);

        // 9. 카카오페이용 금액 정보
        amount = productOrderSubmitService.getAmount(orderNo, userDetails, orderDTO);

        // 10. 세션 저장
        session.setAttribute("orderNo", orderNo);
        session.setAttribute("orderDTO", orderDTO);
        session.setAttribute("amount", amount);
        session.setAttribute("cartNos", cartNos);
        session.setAttribute("itemPointList", itemPointList);

        // 11. 결제 요청
        if ("카카오페이".equals(orderDTO.getPayment())) {
            return kakaoPayService.kakaoPayReady(amount);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("type", "일반");
            return ResponseEntity.ok(response);
        }
    }
}
