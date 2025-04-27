package kr.co.lotteon.entity.article;

import jakarta.persistence.*;
import kr.co.lotteon.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Recruit")
public class Recruit {

    // 채용 게시판

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User user;

    private String title; //제목
    private String department; // 채용부서
    private String career; //경력
    private String employmentType; // 채용형태(정규직, 계약직)
    private String content; //내용

    @CreationTimestamp
    private LocalDateTime wdate; // 등록일
    private LocalDate startDate; //채용 시작일
    private LocalDate endDate;   //채용 마감일
    
    /*
    * 상태에 대한 컬럼을 지워질 수 있음.
    * 이유: 관리자에 마감하는 기능이 없기에 오늘 날짜에 따라 모집, 종료 판단하는게 편함
    * */
    private String state;  // (모집중, 종료)

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "모집중";
        }
    }
}
