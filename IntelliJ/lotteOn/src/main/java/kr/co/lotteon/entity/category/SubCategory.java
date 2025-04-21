package kr.co.lotteon.entity.category;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "SubCategory")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int subCateNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mainCateNo")
    private MainCategory mainCategory;

    private String subCategoryName;

}
