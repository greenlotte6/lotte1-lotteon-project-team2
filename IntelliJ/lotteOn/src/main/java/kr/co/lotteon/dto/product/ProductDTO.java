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
    private MainCategoryDTO mainCateNo;
    private SubCategoryDTO subCateNo;
    private SellerDTO company;

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

    // (선택적으로 보여줄 이름들)
    private String mainCateName;
    private String subCateName;
    private String sellerName;
}
