package kr.co.lotteon.dto.order;

import kr.co.lotteon.dto.product.ProductDTO;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long itemNo;       // 주문 상세 번호

    private int orderNo;      // 주문 번호 (Order FK)
    private int prodNo;       // 상품 번호 (Product FK)

    private int itemPrice;    // 구매 당시 상품 가격
    private int itemDiscount; // 할인율 (퍼센트)
    private int itemCount;    // 수량

    private ProductDTO product;

    private String category;

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
