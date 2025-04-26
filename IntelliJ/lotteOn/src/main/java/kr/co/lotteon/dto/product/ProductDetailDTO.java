package kr.co.lotteon.dto.product;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {

    private int no;

    private String prodNo;           // 상품 번호 (Product FK)

    private String receiptType;   // 영수증 유형
    private String prodState;     // 상품 상태
    private String taxFree;       // 면세 여부
    private String bizType;       // 사업자 유형
    private String origin;        // 원산지

    // 옵션 1~6
    private String opt1;
    private String opt1Cont;

    private String opt2;
    private String opt2Cont;

    private String opt3;
    private String opt3Cont;

    private String opt4;
    private String opt4Cont;

    private String opt5;
    private String opt5Cont;

    private String opt6;
    private String opt6Cont;
}
