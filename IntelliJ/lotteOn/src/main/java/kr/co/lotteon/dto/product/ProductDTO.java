package kr.co.lotteon.dto.product;

import kr.co.lotteon.dto.review.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private int no;
    private String cate;
    private String name;
    private String description;
    private int price;
    private int stock;
    private String rdate;
    private String wdate;
    private String productcol;
    private int salesCount;

    // 추가 컬럼
    private String cname;
    private List<ReviewDTO> reviews;


}
