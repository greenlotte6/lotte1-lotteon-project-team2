package kr.co.lotteon.dto.operation;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {

    private LocalDate date;
    private long orderTotal;
    private long creditTotal;
    private long cancelTotal;

}
