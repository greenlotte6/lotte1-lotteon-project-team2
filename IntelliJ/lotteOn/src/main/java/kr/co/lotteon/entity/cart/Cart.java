package kr.co.lotteon.entity.cart;

import jakarta.persistence.*;
import kr.co.lotteon.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

import kr.co.lotteon.entity.product.Product;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
@Builder
@Entity
@Table(name = "Cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartNo; // 장바구니 번호


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", nullable = false)
    private User user; // 회원 정보 외래키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodNo", nullable = false)
    private Product product; // 상품 번호 외래키

    private int cartProdCount = 1; // 담은 상품 수량 (기본값 1)

    @CreationTimestamp
    private LocalDateTime cartProdDate; // 장바구니에 담은 날짜
}
