package kr.co.lotteon.dto.order;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {

    private LocalDateTime orderDate;

    private int orderNo;
    private String orderStatus;
    private String prodName;
    private String company;
    private int itemCount;
    private int itemPrice;
    private String oNameImage;


}
