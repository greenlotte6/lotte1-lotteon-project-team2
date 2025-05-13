package kr.co.lotteon.dto.category;

import jakarta.persistence.PrePersist;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDTO implements Serializable {

    private int subCateNo;
    private MainCategoryDTO mainCateNo;
    private String subCategoryName;

    private int orderIndex; // 순서
    private String state;

}