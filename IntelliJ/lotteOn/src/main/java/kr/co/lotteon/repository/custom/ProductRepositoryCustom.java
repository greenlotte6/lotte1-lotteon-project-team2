package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {


    Page<Tuple> selectBestAllForList(int subCate);

    Page<Tuple> sortedProducts(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> sortedSearchProducts(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> selectAllForListByRole(PageRequestDTO pageRequestDTO, Pageable pageable);

}
