package kr.co.lotteon.dto.user;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    // 유저 상세정보 DTO

    private int no;
    private int userPoint;

    // VVIP, VIP, GOLD, SILVER, FAMILY
    private String rank;
    private String gender;
    private String content; // 유저 기타 정보(관리자 페이지)

    private UserDTO user;
}
