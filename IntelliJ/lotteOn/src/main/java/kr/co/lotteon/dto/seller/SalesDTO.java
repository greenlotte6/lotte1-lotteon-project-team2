package kr.co.lotteon.dto.seller;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesDTO {

    private String company;
    private String bizRegNo;

    // 주문건수
    private int orderCount;
    // 결제완료
    private int creditCount;
    // 배송중
    private int shippingCount;
    // 배송완료
    private int deliveryCount;
    // 구매확정
    private int confirmCount;
    // 주문합계
    private long orderTotal;
    // 매출총합
    private long total;
}

