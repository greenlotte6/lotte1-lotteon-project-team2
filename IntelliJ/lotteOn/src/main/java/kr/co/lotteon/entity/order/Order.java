package kr.co.lotteon.entity.order;

import jakarta.persistence.*;
import kr.co.lotteon.dto.order.OrderItemDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

import kr.co.lotteon.entity.user.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@Builder
@Entity
@Table(name = "`Order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderNo; // 주문 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user; // 주문자 정보

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems; // 주문 상세 (OrderItem)

    private int orderTotalPrice; // 총 주문 금액

    private String receiverZip; // 우편번호

    private String orderAddr; // 배송 주소

    private String orderStatus; // 주문 상태

    @CreationTimestamp
    private LocalDateTime orderDate; // 주문 일자

    private String orderSender;    // 보내는 사람 이름
    private String senderHp;       // 보내는 사람 연락처
    private String orderReceiver;  // 받는 사람 이름
    private String receiverHp;     // 받는 사람 연락처

    @Lob
    private String orderContent; // 배송 요청사항 등

    private String payment;       // 결제 수단
    private String paymentContent;   // 결제 상세 정보

    @PrePersist
    public void prePersist() {
        if (this.orderStatus == null) {
            this.orderStatus = "입금대기";
        }
    }

}
