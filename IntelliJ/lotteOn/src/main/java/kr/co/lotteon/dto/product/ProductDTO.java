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
@Builder
public class ProductDTO {

    private int prodNo;

    // 카테고리 및 판매자 ID
    private SubCategoryDTO subCate;
    private String company;
    private String thumb;

    // 상품 정보
    private String prodName;
    private int prodPrice;
    private int prodPoint;
    private int prodStock;
    private int prodSold;
    private int prodDiscount;
    private int prodDeliveryFree;
    private String prodContent;

    private LocalDateTime regDate;
    private int hit;

    // 리뷰 정보
    private double ratingTotal;
    private int reviewCount;


}
