package kr.co.lotteon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {

    // 유저 상세정보 DTO

    private int no;
    private int userPoint;
    private String rank;
    private String gender;

    private UserDTO user;
}
