package kr.co.lotteon.dto.operation;

import lombok.*;

/*
* 관리자 운영 정보 출력
* */

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {

    // 문의사항 총 갯수, 오늘 작성수, 어제 작성수
    private long inquiryCountTotal;
    private long inquiryCountToday;
    private long inquiryCountYesterday;

    // 회원가입 총 갯수, 오늘 가입, 어제 가입

    long memberCountTotal;
    long memberCountToday;
    long memberCountYesterday;

    // 주문 총 갯수, 오늘 주문, 어제 주문
    long orderCountTotal;
    long orderCountToday;
    long orderCountYesterday;
    long orderPriceTotal;
    long orderPriceToday;
    long orderPriceYesterday;



}
