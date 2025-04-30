package kr.co.lotteon.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "User")
public class User {

    @Id
    private String uid;     // 아이디
    private String pass;    // 비밀번호
    private String name;    // 이름/회사명
    private String email;   // 이메일
    private String hp;      // 전화번호
    private String role;    // 역할(관리자, 유저, 판매자)
    private String zip;     // 우편번호
    private String addr1;   // 주소
    private String addr2;   // 자세한 주소
    private String regip;   // IP주소
    private String state;   // 상태(정상,중지,휴먼,탈퇴)
    private String ssn; // 주민번호

    @CreationTimestamp
    private LocalDateTime regDate;     // 가입일자

    @CreationTimestamp
    private LocalDateTime updateDate;  // 변경 날짜(마이페이지)
    private LocalDateTime leaveDate;   // 탈퇴일자
    private LocalDateTime lastLoginAt; // 최근 로그인 날짜

    // OAuth 인증 업체 정보
    private String provider;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = "USER";
        } 
        
        if (this.state == null) {
            this.state = "정상";
        }
    }

}
