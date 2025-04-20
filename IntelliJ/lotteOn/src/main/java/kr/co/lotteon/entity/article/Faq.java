package kr.co.lotteon.entity.article;

import jakarta.persistence.*;
import kr.co.lotteon.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Faq")
public class Faq {

    // 자주묻는질문 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User user;

    private String cateV1;  //1차 유형(회원)
    private String cateV2;  //2차 유형(가입,탈퇴)
    private String title;   //제목
    private String content; //내용
    private String hit;     //조회수(관리자 자주묻는질문 목록 5-7-5에 조회수 존재)
    private LocalDateTime wdate; // 등록일
    private String regip;        // 컴퓨터IP

}
