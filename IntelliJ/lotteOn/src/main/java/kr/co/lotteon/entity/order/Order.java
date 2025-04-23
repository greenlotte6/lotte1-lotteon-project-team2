package kr.co.lotteon.entity.order;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import kr.co.lotteon.entity.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@Builder
@Entity
@Table(name = "Order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderNo; // 주문 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user; // 주문자 정보

    private int orderTotalPrice; // 총 주문 금액

    private String orderAddr; // 배송 주소

    private String orderStatus = "결제대기"; // 주문 상태

    @CreationTimestamp
    private LocalDateTime orderDate; // 주문 일자

    private String orderSender;    // 보내는 사람 이름
    private String senderHp;       // 보내는 사람 연락처
    private String orderReceiver;  // 받는 사람 이름
    private String receiverHp;     // 받는 사람 연락처

    @Lob
    private String orderContent; // 배송 요청사항 등

    private String payment;       // 결제 수단
    private String paymentCont;   // 결제 상세 정보

    private String color; // 상품 색상
    private int size;  // 상품
}
