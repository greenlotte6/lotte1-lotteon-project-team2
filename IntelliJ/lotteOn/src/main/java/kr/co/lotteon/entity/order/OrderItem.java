package kr.co.lotteon.entity.order;

import jakarta.persistence.*;
import lombok.*;

import kr.co.lotteon.entity.product.Product;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"order", "product"})
@Builder
@Entity
@Table(name = "OrderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemNo; // 주문 상세 번호 (기본키)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderNo", nullable = false)
    private Order order; // 주문 (Order 테이블과 연관)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodNo", nullable = false)
    private Product product; // 상품 (Product 테이블과 연관)

    private int itemPrice;    // 구매 당시 상품 가격
    private int itemDiscount; // 할인율 (퍼센트)
    private int itemCount = 1; // 구매 수량 (기본값 1)
}
