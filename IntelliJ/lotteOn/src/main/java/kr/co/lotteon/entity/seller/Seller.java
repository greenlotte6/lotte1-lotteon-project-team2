package kr.co.lotteon.entity.seller;


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
@Table(name = "Seller")
public class Seller {

    // 판매자 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sno;

    @OneToOne
    @JoinColumn(name="uid")
    private User user;

    private String company; //회사명
    private String fax; //포인트 사용,적립 내역
    private String ceo; //대표
    private String bizRegNo; //사업자등록번호
    private String commerceNo; //통신판매업번호
    /*
    *   rank : 판매자 등급 ( 카테고리별 상품목록 출력하기 / 2-1)
    *   판매자 등급에 따른 뱃지 출력
    *   BRONZE, SILVER, GOLD, PLATINUM 지정
    * */

    @Column(name = "`rank`")
    private String rank; //판매자 등급

}
