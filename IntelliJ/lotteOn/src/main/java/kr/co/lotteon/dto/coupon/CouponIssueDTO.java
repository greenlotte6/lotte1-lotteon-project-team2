package kr.co.lotteon.dto.coupon;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.lotteon.dto.user.UserDTO;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueDTO {

    // 발급 쿠폰 DTO

    private long issueNo;
    private UserDTO user;
    private CouponDTO coupon;
    private String state; // 상태(사용, 미사용)

    private LocalDate usedDate; //사용일
    private LocalDate regDate;  //등록일

    private String validTo; // 쿠폰 마감일

    private String issuedBy; // 발급자

    private String cno;

    // 목록용 변수
    private String uid;


}


