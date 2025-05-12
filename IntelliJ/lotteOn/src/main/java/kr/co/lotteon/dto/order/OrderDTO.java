package kr.co.lotteon.dto.order;

import kr.co.lotteon.dto.user.UserDTO;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private int orderNo;           // 주문 번호

    private String uid;            // 주문자 아이디 (User.uid)

    private int orderTotalPrice;   // 총 주문 금액
    private String orderAddr;      // 배송 주소
    private String orderStatus;    // 주문 상태
    private LocalDateTime orderDate; // 주문 일자
    private String orderSender;    // 보내는 사람
    private String senderHp;       // 보내는 사람 연락처

    private String orderReceiver;  // 받는 사람
    private String receiverZip;    // 받는 사람 우편 번호
    private String receiverHp;     // 받는 사람 연락처
    private String orderContent;   // 배송 요청사항
    private String payment;        // 결제 수단
    private String paymentContent;    // 결제 상세 정보

    private int totalQuantity;
    private int originalTotalPrice;
    private int shippingFee;
    private int totalDiscount;
    private int pointDiscount;
    private int couponDiscount;
    private int totalPoint;

    private int count; // itemOrder 갯수 (관리자 페이지 사용)

    private UserDTO user;
    // 상품 정보
    private List<OrderItemDTO> orderItems;

}
