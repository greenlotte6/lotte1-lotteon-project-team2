package kr.co.lotteon.entity.config;

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
@Table(name = "Version")
public class Version {

    // 버전 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User user;

    private String version; //버전정보
    private LocalDateTime wdate; // 등록일
    private String content; //내용

}
