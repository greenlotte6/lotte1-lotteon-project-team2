package kr.co.lotteon.dto.order;

import kr.co.lotteon.dto.product.ProductDTO;
import kr.co.lotteon.dto.product.ProductImageDTO;
import kr.co.lotteon.dto.seller.SellerDTO;
import lombok.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {

    private SellerDTO seller;
    private OrderItemDTO orderItem;
    private ProductDTO product;

    // 상품이미지
    private String productImage;

    // 주문데이터
    private int orderNo;
    private LocalDateTime orderDate;
    private String orderStatus;

}
