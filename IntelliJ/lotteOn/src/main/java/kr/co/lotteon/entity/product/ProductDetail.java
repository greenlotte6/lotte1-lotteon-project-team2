package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

/**
 * 상품 상세 옵션 및 부가정보를 담는 엔티티
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
@Builder
@Entity
@Table(name = "ProductDetails")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no; // 상품 디테일 번호

    private String receiptType; // 영수증 유형

    @OneToOne
    @JoinColumn(name="prodNo")
    private Product product;

    private String prodState; // 상품 상태
    private String taxFree; // 면세 여부
    private String bizType; // 사업자 유형
    private String origin; // 원산지

    // 옵션 1
    private String opt1;
    private String opt1Cont;

    // 옵션 2
    private String opt2;
    private String opt2Cont;

    // 옵션 3
    private String opt3;
    private String opt3Cont;

    // 옵션 4
    private String opt4;
    private String opt4Cont;

    // 옵션 5
    private String opt5;
    private String opt5Cont;

    // 옵션 6
    private String opt6;
    private String opt6Cont;
}
