package kr.co.lotteon.dto.config;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TermsDTO {

    private int no;
    private String terms;    //이용약관(일반유저)
    private String tax;      //판매자이용약관(판매자)
    private String finance;  //전자금융 이용이용약관
    private String privacy;  //개인정보 수집동의
    private String location; //위치정보 이용약관

    /*
    * 정책 페이지에서 제1조, 제2조, 제3조 ... 를 위한 변수 
    * */
    private String section1;
    private String section2;
    private String section3;
    private String section4;
    private String section5;
    private String section6;



}