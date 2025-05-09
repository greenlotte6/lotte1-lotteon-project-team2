package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Tuple> selectAllSales(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> orderInfoPaging(PageRequestDTO pageRequestDTO, Pageable pageable, String uid);

    Page<Tuple> selectAllOrder(PageRequestDTO pageRequestDTO, Pageable pageable);
}
