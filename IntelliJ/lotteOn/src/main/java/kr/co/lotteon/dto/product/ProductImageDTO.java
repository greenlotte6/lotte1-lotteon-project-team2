package kr.co.lotteon.dto.product;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {

    private int ino;

    private ProductDTO prodNo;  // 상품 번호 (Product FK)

    // 목록용 이미지
    private String oNameList;
    private String sNameList;

    // 메인 이미지
    private String oNameMain;
    private String sNameMain;

    // 상세 이미지
    private String oNameDetail;
    private String sNameDetail;

    // 썸네일 이미지
    private String oNameThumb3;
    private String sNameThumb3;

    private LocalDateTime rdate;
}
