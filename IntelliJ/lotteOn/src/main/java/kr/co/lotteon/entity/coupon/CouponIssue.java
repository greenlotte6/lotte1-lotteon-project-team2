package kr.co.lotteon.entity.coupon;

import jakarta.persistence.*;
import kr.co.lotteon.entity.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "CouponIssue")
public class CouponIssue {

    /*
    * 쿠폰 테이블(유저가 사용하는 쿠폰)
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long issueNo;

    @ManyToOne
    @JoinColumn(name="uid")
    private User user;

    @ManyToOne
    @JoinColumn(name="cno")
    private Coupon coupon;

    private String state; // 상태(사용, 미사용)
    
    private LocalDate usedDate; //사용일

    @CreationTimestamp
    private LocalDate regDate; // 발급일

    private String issuedBy; // 발급자

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "미사용";
        }

    }

}
