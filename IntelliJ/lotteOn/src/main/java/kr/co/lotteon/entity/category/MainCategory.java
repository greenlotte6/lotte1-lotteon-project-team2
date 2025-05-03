package kr.co.lotteon.entity.category;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "MainCategory")
public class MainCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mainCateNo;
    private String mainCategoryName;
    private int orderIndex; // 순서
    private String state;

    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = "활성";
        }
    }

}
