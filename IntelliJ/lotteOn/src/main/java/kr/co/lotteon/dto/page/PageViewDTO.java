package kr.co.lotteon.dto.page;

import kr.co.lotteon.dto.coupon.CouponDTO;
import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageViewDTO {

    // 상품
    ProductDTO productDTO;
    // 쿠폰
    List<CouponDTO> couponDTOList;
    // 상품 상세
    ProductDetailDTO productDetailDTO;
    // 리뷰
    PageResponseDTO reviewPageResponseDTO;
    // qna
    PageResponseDTO inquiryPageResponseDTO ;

}
