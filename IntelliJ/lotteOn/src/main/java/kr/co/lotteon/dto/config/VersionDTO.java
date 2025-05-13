package kr.co.lotteon.dto.config;

import kr.co.lotteon.dto.user.UserDTO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VersionDTO implements Serializable {

    private static final long serialVersionUID = 13222L;

    // 버전 정보 DTO

    private int vno;

    private UserDTO user;

    private String version; //버전정보
    private LocalDateTime wdate; // 등록일
    private String content; //내용
    
    // 페이지 목록 출력용 유저 아이디
    private String uid;
}
