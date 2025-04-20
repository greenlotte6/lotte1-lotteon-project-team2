package kr.co.lotteon.dto.config;

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
public class VersionDTO {

    // 버전 정보 DTO

    private int vno;

    private User user;

    private String version; //버전정보
    private LocalDateTime wdate; // 등록일
    private String content; //내용
}
