package kr.co.lotteon.dto.kakao;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Amount {

    private String cid = "TC0ONETIME";                  // 가맹점 코드 (예: "TC0ONETIME")
    private String partner_order_id;                    // 주문 번호 (예: "12345")
    private String partner_user_id;                     // 회원 ID (예: "user123")
    private String item_name;                          // 상품명 (예: "상품 A")
    private int quantity;                              // 수량 (예: 1)
    private int total;                                 // 총 결제 금액
    private int tax_free= 0;                           // 비과세 금액
    private int tax;                                   // 부가세 금액

    private int point;                                 // 사용한 포인트
    private int discount;                              // 할인금액
    private int green_deposit;                         // 컵 보증금

}