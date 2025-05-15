package kr.co.lotteon.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 12;


    // 추가 필드 (검색)
    private String keyword;
    private String searchType;
    private String subKeyword;

    private int maxPrice;
    private int minPrice;

    private String startDate;
    private String endDate;

    private LocalDate start;
    private LocalDate end;

    // 추가 필드 (상품 목록 정렬용)
    private String sortType;
    private int subCateNo; // 하위 카테고리
    private String period; // 판매 많은 순, 후기 많은 순
    
    // 등급(관리자 검색 사용)
    private String role;
    private String uid;

    // 추가 필드 (상품 보기)
    private String prodNo;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());

    }

}
