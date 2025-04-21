package kr.co.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
@Builder
@Entity
@Table(name = "ProductImage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ino; // 기본 키


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodNo", nullable = false) // 상품 번호 외래키
    private Product product;

    // 목록용 이미지 이름
    private String oNameList;
    private String sNameList;

    // 메인 이미지 이름
    private String oNameMain;
    private String sNameMain;

    // 상세 이미지 이름
    private String oNameDetail;
    private String sNameDetail;

    // 썸네일 이미지
    private String oNameThumb3;
    private String sNameThumb3;

    @CreationTimestamp
    private LocalDateTime rdate; // 등록일
}
