package kr.co.lotteon.dto.article;

import kr.co.lotteon.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {

    // 공지사항 DTO

    private int no;

    private User user;

    /*
     * 관리자 공지사항 수정(5-7-4)
     * 유형 선택하는 부분이 있음.
     * */
    private String cate; //고객서비스/이벤트 당첨/안전거래/고객서비스/위해상품 등
    private String title; //제목
    private String content; //내용
    private int hit;
    private LocalDateTime wdate; // 등록일
    private String regip;  // 컴퓨터IP
}
