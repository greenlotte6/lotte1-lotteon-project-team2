package kr.co.lotteon.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainCategoryDTO {

    private int mainCateNo;
    private String mainCategoryName;

}