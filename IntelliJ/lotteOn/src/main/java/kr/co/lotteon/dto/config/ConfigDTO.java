package kr.co.lotteon.dto.config;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigDTO {

    // 사이트설정 DTO

    private int cno; //환경설정 번호

    /*
     * 사이트 정보
     * */
    private String title;      //제목(사이트 브라우저 탭, 헤더 푸터 노출 정보)
    private String subTitle;   //부제(사이트 브라우저 탭, 헤더 푸터 노출 정보)
    private String headerLogo; //헤더 로고(브라우저 탭, 헤더 푸터 노출 로고)
    private String footerLogo; //푸터 로고(브라우저 탭, 헤더 푸터 노출 로고)
    private String favicon;    //파비콘

    /*
     * 기업 정보
     * */
    private String companyName; //상호명
    private String ceoName; //대표이사
    private String businessNumber; //사업자등록번호
    private String onlineSalesNumber; //통신판매업신고번호
    private String addr1; //기본주소
    private String addr2; //상세주소

    /*
     * 고객센터 정보/카피라이트
     * */
    private String hp; //전화번호
    private String workingHours; //업무시간
    private String email; //이메일
    private String efinDispute; //전자금융거래 분쟁담당
    private String copyright; // 카피라이트(푸터에 노출되는 카피라이트 정보)

}
