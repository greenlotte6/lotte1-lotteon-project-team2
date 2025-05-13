package kr.co.lotteon.entity.delivery;

import jakarta.persistence.*;
import kr.co.lotteon.entity.order.Order;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Delivery")
public class Delivery {

    @Id
    private long dno; // 운송 번호 trackingNumber

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderNo", nullable = false)
    private Order order;

    private String deliveryCompany;

    private String state;

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "배송준비";
        }
    }

    @CreationTimestamp
    private LocalDateTime deliveryDate;
}
