package kr.co.lotteon.dto.article;

import kr.co.lotteon.dto.user.UserDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO implements Serializable {

    // 문의하기 DTO

    private int no;

    private UserDTO user;
    private String uid;

    private String prodNo;

    private String cateV1; //1차 유형(회원)
    private String cateV2; //2차 유형(가입,탈퇴)
    private String title; //제목
    private String content; //내용

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate wdate; // 등록일
    private String state;  // (검토 중, 답변완료)
    private String regip;  // 컴퓨터IP

    /*
     * 답변 테이블을 따로 만들까 하다가. GPT한테 물어보니 대부분 질문 게시판 안에
     * 답변 컬럼을 추가하는 방식을 사용한다고 하길래 컬럼 추가
     * */
    private String answer; // 답변

    /*
     * 마이페이지 문의하기(6-8)
     * 테이블 안에 문의채널 속성이 존재함.
     * 고객센터, 판매자 게시판으로 나누어져 있기에 그걸 구분하기 위한 컬럼
     * */
    private String channel; // (고객센터,판매자)
    private String email;   // 마이페이지 문의하기에서 입력하는 email

    private int password; // 비밀번호 4자리

}
