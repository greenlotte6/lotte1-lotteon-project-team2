package kr.co.lotteon.repository.custom;

import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {


    Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> selectBestAllForList(String cate);
}
