package kr.co.lotteon.dto.product;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDTO implements Serializable {

    private int ino;

    private ProductDTO productDTO;
    private String productNo; // 상품 번호 (Product FK)

    // 목록용 이미지
    private String oNameList;
    private String sNameList;

    // 메인 이미지
    private String oNameMain;
    private String sNameMain;

    // 상세 이미지(상세페이지 설명에 들어가는 큰 이미지)
    private String oNameDetail;
    private String sNameDetail;

    // 썸네일 이미지(상세페이지 섬네일 이미지)
    private String oNameThumb3;
    private String sNameThumb3;

    private LocalDateTime rdate;

    // 이미지첨부 객체
    private MultipartFile file1; // 목록
    private MultipartFile file2; // 메인
    private MultipartFile file3; // 상세(작은것)
    private MultipartFile file4; // 상품상세정보(큰것)
}
