package kr.co.lotteon.dto.seller;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerDTO {

    // 판매자 DTO

    private int sno;

    private UserDTO user;

    private String company; //회사명
    private String fax; //포인트 사용,적립 내역
    private String ceo; //대표
    private String bizRegNo; //사업자등록번호
    private String commerceNo; //통신판매업번호

    /*
     *   rank : 판매자 등급 ( 카테고리별 상품목록 출력하기 / 2-1)
     *   판매자 등급에 따른 뱃지 출력
     *   임의로 FAMILY, SILVER, GOLD, VIP, VVIP 지정
     * */
    private String rank; //판매자 등급

}
