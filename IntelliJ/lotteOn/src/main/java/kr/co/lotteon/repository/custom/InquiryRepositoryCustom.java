package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.article.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface InquiryRepositoryCustom {


    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> selectAllQnaByType(PageRequestDTO pageRequestDTO, Pageable pageable);
}
