package kr.co.lotteon.dto.coupon;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.lotteon.entity.coupon.Coupon;
import kr.co.lotteon.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueDTO {

    // 발급 쿠폰 DTO

    private int issueNo;
    private User user;
    private Coupon coupon;
    private String state; // 상태(사용, 미사용)

    private LocalDate usedDate; //사용일
    private LocalDate regDate;  //등록일

    private String issuedBy; // 발급자
}
