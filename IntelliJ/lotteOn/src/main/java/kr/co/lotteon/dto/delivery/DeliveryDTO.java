package kr.co.lotteon.dto.delivery;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {

    private int dno;

    private int orderNo;

    private String deliveryCompany;

    private String trackingNumber;

    private String state;
}
