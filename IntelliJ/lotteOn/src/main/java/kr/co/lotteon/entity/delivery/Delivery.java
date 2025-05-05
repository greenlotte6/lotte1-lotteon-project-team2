package kr.co.lotteon.entity.delivery;

import jakarta.persistence.*;
import kr.co.lotteon.entity.order.Order;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderNo", nullable = false)
    private Order order;

    private String deliveryCompany;

    private String trackingNumber;

    private String state;
}
