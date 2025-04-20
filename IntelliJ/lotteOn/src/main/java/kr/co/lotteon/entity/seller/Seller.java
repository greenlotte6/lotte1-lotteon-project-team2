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
    private String rank; //판매자 등급

}
