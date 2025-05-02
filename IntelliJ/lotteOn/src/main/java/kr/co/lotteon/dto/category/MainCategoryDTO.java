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

    private List<SubCategoryDTO> subCategories;

}