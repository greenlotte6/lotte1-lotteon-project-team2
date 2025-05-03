package kr.co.lotteon.dto.category;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryDTO {

    private int mainCateNo;
    private String mainCategoryName;
    private int orderIndex; // 순서
    private String state; // 상태

    private List<SubCategoryDTO> subCategories;

}