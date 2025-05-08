// redis 방문자 집계 엔티티


package kr.co.lotteon.entity.visitor;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Daily_visitor_stats")
public class DailyVisitorStats {

    @Id
    // 아아디
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 방문 날짜
    @Column(unique = true,nullable = false)
    private LocalDate visitDate;


    // 고유 방문자
    @Column(nullable = false)
    private Integer uniqueVisitors;
}
