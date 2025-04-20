package kr.co.lotteon.dto.feedback;


import kr.co.lotteon.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDTO {

    // 교환 DTO

    private int eno;
    private User user;
    private String type;    //반품 유형(단순 변심/파손 및 불량/주문 실수/기타)
    private String content; //사유 입력
    private String sName;   //이미지 변환이름
    private String oName;   //이미지 기존이름
}
