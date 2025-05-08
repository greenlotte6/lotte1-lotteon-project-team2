package kr.co.lotteon.entity.feedback;

import jakarta.persistence.*;
import kr.co.lotteon.entity.order.OrderItem;
import kr.co.lotteon.entity.user.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "`Return`")
public class Return {

    // 반품 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writer")
    private User user;

    private String type;    //반품 유형(단순 변심/파손 및 불량/주문 실수/기타)
    private String content; //사유 입력
    private String sName;   //이미지 변환이름
    private String oName;   //이미지 기존이름

    @OneToOne
    @JoinColumn(name="itemNo")
    private OrderItem orderItem;
}
