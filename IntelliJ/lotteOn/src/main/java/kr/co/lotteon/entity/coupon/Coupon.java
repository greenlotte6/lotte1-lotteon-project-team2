package kr.co.lotteon.entity.coupon;

import jakarta.persistence.*;
import kr.co.lotteon.entity.user.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "Coupon")
public class Coupon {

    /*
    * 쿠폰 테이블(관리자, 판매자가 등록하는 테이블)
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cno;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    private String issuedBy; //발급처
    private String couponType; //쿠폰 종류(개별상품할인(판매자), 주문상품할인(관리자), 배송비 무료(관리자))
    private String couponName; //쿠폰 이름
    private String benefit; //혜택 (2,000 할인, 15% 할인 등)

    private LocalDate validFrom; //사용기간 시작일
    private LocalDate  validTo; //사용기간 만료일
    private int validDaysAfterIssue; //발급일로부터 ~일 이내

    private String caution; // 유의사항
    private String state; // 상태(발급중, 종료)

    private int usedCount; // 사용한 횟수
    private int issueCount; // 발급한 쿠폰수

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "발급중";
        }

    }

}
