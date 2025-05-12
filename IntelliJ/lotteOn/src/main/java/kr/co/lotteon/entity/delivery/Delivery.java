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
    private long dno;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderNo", nullable = false)
    private Order order;

    private String deliveryCompany;

    private String trackingNumber;

    private String state;

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "배송준비";
        }
    }
}
