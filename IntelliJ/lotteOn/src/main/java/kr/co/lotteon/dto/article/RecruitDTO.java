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
public class RecruitDTO {

    // 채용 게시판 DTO

    private int no;

    private User user;

    private String title; //제목
    private String department; // 채용부서
    private String career; //경력
    private String employmentType; // 채용형태(정규직, 계약직)
    private String content; //내용
    private LocalDateTime wdate; // 등록일
    private LocalDateTime startDate; //채용 시작일
    private LocalDateTime endDate;   //채용 마감일

    /*
     * 상태에 대한 컬럼을 지워질 수 있음.
     * 이유: 관리자에 마감하는 기능이 없기에 오늘 날짜에 따라 모집, 종료 판단하는게 편함
     * */
    private String state;  // (모집중, 종료)


}
