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
@Table(name = "Inquiry")
public class Inquiry {

    // 문의하기 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User user;

    private String prodNo;

    private String cateV1; //1차 유형(회원)
    private String cateV2; //2차 유형(가입,탈퇴)
    private String title; //제목
    private String content; //내용

    @CreationTimestamp
    private LocalDateTime wdate; // 등록일
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

    /*
    * 상품 페이지에서 출력하기 위한 상품 코드
    * */

    private int password; // 비밀번호 4자리

    @PrePersist
    public void prePersist() {
        if (this.channel == null) {
            this.channel = "고객센터";
        }
        if(this.state == null){
            this.state = "검토중";
        }
    }


}
