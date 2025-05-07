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

    private long memberCountTotal;
    private long memberCountToday;
    private long memberCountYesterday;

    // 주문 총 갯수, 오늘 주문, 어제 주문
    private long orderCountTotal;
    private long orderCountToday;
    private long orderCountYesterday;
    private long orderPriceTotal;
    private long orderPriceToday;
    private long orderPriceYesterday;
    
    // 입금대기, 배송준비, 취소요청, 교환요청, 반품요청
    private long readyTotal;
    private long deliveryTotal;
    private long cancelTotal;
    private long exchangeTotal;
    private long returnTotal;
    
    // 매출
    private String sale1; //1등 매출명
    private long sale1Total; //1등 매출
    private String sale2; //2등 매출명
    private long sale2Total; //2등 매출
    private String sale3; //2등 매출명
    private long sale3Total; //3등 매출
    private String sale4; //4등 매출명
    private long sale4Total; //4등 매출


}
