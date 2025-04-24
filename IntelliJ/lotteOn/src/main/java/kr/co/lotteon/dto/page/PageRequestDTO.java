package kr.co.lotteon.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 12;

    private String searchType;
    private String keyword;

    // 추가 필드 (상품 목록 정렬용)
    private String sortType;
    private int subCateNo; // 하위 카테고리
    private String period; // 판매 많은 순, 후기 많은 순

    // 추가 필드 (상품 보기)
    private String prodNo;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());

    }

}
