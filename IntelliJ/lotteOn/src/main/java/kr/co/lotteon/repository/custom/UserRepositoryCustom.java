package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
    Page<Tuple> selectAllUser(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> selectAllPoint(PageRequestDTO pageRequestDTO, Pageable pageable);
}
