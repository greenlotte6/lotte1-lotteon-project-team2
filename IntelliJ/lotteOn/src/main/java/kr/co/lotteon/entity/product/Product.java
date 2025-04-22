package kr.co.lotteon.entity.product;


import jakarta.persistence.*;
import kr.co.lotteon.entity.category.MainCategory;
import kr.co.lotteon.entity.category.SubCategory;
import kr.co.lotteon.entity.seller.Seller;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Product")
public class Product {

    @Id
    private String prodNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subCateNo") // subCategory 외래키
    private SubCategory subCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellor") // seller 외래키
    private Seller seller;

    private String prodName; // 상품명
    private int prodPrice; // 상품 가격
    private int prodPoint; // 상품 포인트
    private int prodStock; // 상품 재고
    private int prodSold; // 상품 판매 수
    private int prodDiscount; // 상품 할인
    private int prodDeliveryFee; // 상품 무료배송
    private String prodContent; // 상품 내용

    @CreationTimestamp
    private LocalDateTime regDate; // 상품 등록일
    private int hit; // 상품 조회수

    private double ratingTotal; // 상품 리뷰 총 점
    private int reviewCount;    // 상품 리뷰 총 수
    private double ratingAvg;   // 상품 리뷰 평균
    private String prodBrand;


}
