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
    private ProductDTO productDTO;

    // 쿠폰
    private List<CouponDTO> couponDTOList;

    // 상품 상세
    private ProductDetailDTO productDetailDTO;

    // 리뷰
    private PageResponseDTO reviewPageResponseDTO;

    // QnA
    // private PageResponseDTO inquiryPageResponseDTO;
}
