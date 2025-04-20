package kr.co.lotteon.dto.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermsDTO {

    private int no;
    private String terms;    //이용약관(일반유저)
    private String tax;      //판매자이용약관(판매자)
    private String finance;  //전자금융 이용이용약관
    private String privacy;  //개인정보 수집동의
    private String location; //위치정보 이용약관

}