package kr.co.lotteon.dto.category;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryDTO {

    private int mainCateNo;
    private String mainCategoryName;

}