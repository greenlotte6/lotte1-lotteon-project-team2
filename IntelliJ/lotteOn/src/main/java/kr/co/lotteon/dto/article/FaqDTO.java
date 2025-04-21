package kr.co.lotteon.dto.article;

import kr.co.lotteon.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaqDTO {

    // 자주묻는질문 DTO

    private int no;

    private UserDTO user;

    private String cateV1;  //1차 유형(회원)
    private String cateV2;  //2차 유형(가입,탈퇴)
    private String title;   //제목
    private String content; //내용
    private String hit;     //조회수(관리자 자주묻는질문 목록 5-7-5에 조회수 존재)
    private LocalDateTime wdate; // 등록일
    private String regip;        // 컴퓨터IP
}
