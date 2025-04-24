package kr.co.lotteon.dto.coupon;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.co.lotteon.dto.user.UserDTO;
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
public class CouponDTO {

    // 쿠폰 DTO

    private long cno;

    private UserDTO user;

    private String issuedBy; //발급처
    private String couponType; //쿠폰 종류(개별상품할인(판매자), 주문상품할인(관리자), 배송비 무료(관리자))
    private String couponName; //쿠폰 이름
    private String benefit; //혜택 (2,000 할인, 15% 할인 등)

    private LocalDate validFrom; //사용기간 시작일
    private LocalDate validTo; //사용기간 만료일
    private LocalDate regDate; //발급일
    private int validDaysAfterIssue; //발급일로부터 ~일 이내

    private String caution; // 유의사항
    private String state; // 상태(발급중, 종료)

    private int usedCount; // 사용한 횟수
    private int issueCount; // 발급한 쿠폰수
}
