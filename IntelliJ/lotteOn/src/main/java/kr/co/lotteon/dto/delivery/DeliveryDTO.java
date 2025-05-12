package kr.co.lotteon.dto.delivery;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    private long dno;

    private int orderNo; // 주문번호

    private String receiver; // 받는사람
    private String zip; // 우편번호
    private String addr; // 주소
    private String trackingNumber; // 운송장번호
    private String deliveryCompany; // 택배회사

    private String state;

}
