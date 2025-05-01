package kr.co.lotteon.dto.cart;

import lombok.*;


import java.time.LocalDateTime;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private int cartNo;

    private String uid;

    private String prodNo;

    private int cartProdCount;

    private LocalDateTime cartProdDate;

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
