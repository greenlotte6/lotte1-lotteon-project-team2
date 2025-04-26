package kr.co.lotteon.dto.category;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDTO {

    private int subCateNo;
    private MainCategoryDTO mainCateNo;
    private String subCategoryName;

}