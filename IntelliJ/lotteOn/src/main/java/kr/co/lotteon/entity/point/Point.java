package kr.co.lotteon.entity.point;

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
@Table(name = "Point")
public class Point {

    // 포인트 기록 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String pointNo;

    private int point; //적립,사용 포인트 양
    private String pointDesc; //포인트 사용,적립 내역
    private LocalDateTime pointDate; //포인트 적립,사용일
    private LocalDateTime expiryDate; //만료날짜

    @OneToOne
    @JoinColumn(name="uid")
    private User user;
}
