package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SellerRepositoryCustom {
    Page<Tuple> selectAllSellerByType(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> selectAllSellerByTypeAndKeyword(PageRequestDTO pageRequestDTO, Pageable pageable);
}
