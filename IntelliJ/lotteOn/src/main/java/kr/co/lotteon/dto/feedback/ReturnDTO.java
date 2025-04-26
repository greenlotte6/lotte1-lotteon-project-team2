package kr.co.lotteon.dto.feedback;

import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.user.User;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDTO {

    // 반품 DTO

    private int rno;
    private UserDTO user;
    private String type;    //반품 유형(단순 변심/파손 및 불량/주문 실수/기타)
    private String content; //사유 입력
    private String sName;   //이미지 변환이름
    private String oName;   //이미지 기존이름
}
