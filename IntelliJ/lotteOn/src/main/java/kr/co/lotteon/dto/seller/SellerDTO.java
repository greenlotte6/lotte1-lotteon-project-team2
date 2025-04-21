package kr.co.lotteon.dto.seller;

import kr.co.lotteon.dto.user.UserDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDTO {

    private int sno;          // 판매자 번호 (PK)

    private UserDTO uid;      // 회원 아이디 (User FK)

    private String company;   // 회사명
    private String fax;       // 팩스
    private String ceo;       // 대표자 이름
    private String bizRegNo;  // 사업자등록번호
    private String commerceNo;// 통신판매업번호
    private String rank;      // 판매자 등급
}
