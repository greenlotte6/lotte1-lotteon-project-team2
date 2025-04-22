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
    private String mainCategoryName;
    private int subCateNo;
    private String company;
    private String thumb;

    // 상품 정보
    private String prodName;
    private String prodBrand;
    private int prodPrice;
    private int prodPoint;
    private int prodStock;
    private int prodSold;
    private int prodDiscount;
    private int prodDeliveryFee;
    private String prodContent;

    private LocalDateTime regDate;
    private int hit;

    // 리뷰 정보
    private double ratingTotal;
    private int reviewCount;
    private double ratingAvg;


}
