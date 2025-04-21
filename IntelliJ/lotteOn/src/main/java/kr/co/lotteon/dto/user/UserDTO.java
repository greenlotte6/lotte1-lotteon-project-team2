package kr.co.lotteon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO{

    private String uid;     // 아이디
    private String pass;    // 비밀번호
    private String pass2;   // 비밀번호 확인용 entity엔 없음
    private String name;    // 이름/회사명
    private String email;   // 이메일
    private String hp;      // 전화번호
    private String role;    // 역할(관리자, 유저, 판매자)
    private String zip;     // 우편번호
    private String addr1;   // 주소
    private String addr2;   // 자세한 주소
    private String regip;   // IP주소
    private String state;   // 상태(정상,중지,휴먼,탈퇴)

    private LocalDateTime regDate;     // 가입일자
    private LocalDateTime leaveDate;   // 탈퇴일자
    private LocalDateTime lastLoginAt; // 최근 로그인 날짜

    // OAuth 인증 업체 정보
    private String provider;

}