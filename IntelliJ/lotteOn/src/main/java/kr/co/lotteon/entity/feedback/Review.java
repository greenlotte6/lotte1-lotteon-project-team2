package kr.co.lotteon.entity.feedback;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import kr.co.lotteon.entity.user.User;
import kr.co.lotteon.entity.product.Product;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"writer", "product"})
@Builder
@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno; // 리뷰 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer", nullable = false)
    private User writer; // 작성자 (User의 uid와 연결)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodNo", nullable = false)
    private Product product; // 리뷰 대상 상품

    private String content; // 리뷰 내용

    @CreationTimestamp
    private LocalDateTime wdate; // 리뷰 작성일

    private BigDecimal rating; // 평점

    private String sNameImage1;
    private String oNameImage1;
    private String SNameImage2;
    private String oNameImage2;


}