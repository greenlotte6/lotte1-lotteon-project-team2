package kr.co.lotteon.entity.article;

import jakarta.persistence.*;
import kr.co.lotteon.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Notice")
public class Notice {

    // 공지사항 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User user;

    /*
    * 관리자 공지사항 수정(5-7-4)
    * 유형 선택하는 부분이 있음.
    * */
    private String cate; //고객서비스/이벤트 당첨/안전거래/고객서비스/위해상품 등
    private String title; //제목
    private String content; //내용
    private int hit;

    @CreationTimestamp
    private LocalDateTime wdate; // 등록일
    private String regip;  // 컴퓨터IP

}
