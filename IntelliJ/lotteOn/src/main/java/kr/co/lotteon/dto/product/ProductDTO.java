package kr.co.lotteon.dto.product;

import kr.co.lotteon.dto.category.MainCategoryDTO;
import kr.co.lotteon.dto.category.SubCategoryDTO;
import kr.co.lotteon.dto.seller.SellerDTO;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDTO {

    private String prodNo;

    // 카테고리 및 판매자 ID
    private int subCateNo;

    private String company;
    private String thumb;

    private SellerDTO seller;
    private SubCategoryDTO subCategory;

    // 상품 정보
    private String prodName; // 상품명
    private int prodPrice; // 상품 가격
    private int prodPoint; // 상품 포인트
    private int prodStock; // 상품 재고
    private int prodSold; // 상품 판매 수
    private int prodDiscount; // 상품 할인
    private int prodDeliveryFee; // 상품 배송비
    private String prodContent;   // 상품 내용

    private LocalDateTime regDate; // 상품 등록일
    private int hit; // 상품 조회수

    // 리뷰 정보
    private double ratingTotal; // 상품 리뷰 총 점
    private int reviewCount;    // 상품 리뷰 총 수
    private double ratingAvg;   // 상품 리뷰 평균
    private String prodBrand;


}
