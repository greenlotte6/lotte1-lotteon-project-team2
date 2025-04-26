package kr.co.lotteon.dto.point;

import kr.co.lotteon.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointDTO {

    // 포인트 기록 DTO

    private String pointNo;

    private int point; //적립,사용 포인트 양
    private String pointDesc; //포인트 사용,적립 내역
    private LocalDateTime pointDate; //포인트 적립,사용일
    private LocalDateTime expiryDate; //만료날짜

    private UserDTO user;
}
