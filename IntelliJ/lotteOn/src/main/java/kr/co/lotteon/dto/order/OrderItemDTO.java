package kr.co.lotteon.dto.order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderItemDTO {

    private int itemNo;       // 주문 상세 번호

    private int orderNo;      // 주문 번호 (Order FK)
    private int prodNo;       // 상품 번호 (Product FK)

    private int itemPrice;    // 구매 당시 상품 가격
    private int itemDiscount; // 할인율 (퍼센트)
    private int itemCount;    // 수량

}
