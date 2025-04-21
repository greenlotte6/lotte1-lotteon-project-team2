package kr.co.lotteon.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int no = 1;

    @Builder.Default
    private String cate = "free";

    @Builder.Default
    private int pg = 1;

    @Builder.Default
    private int size = 10;

    private String searchType;
    private String keyword;


    public PageRequest getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());

    }

}
