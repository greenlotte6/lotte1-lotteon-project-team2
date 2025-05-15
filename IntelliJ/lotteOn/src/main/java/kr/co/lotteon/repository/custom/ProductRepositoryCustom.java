package kr.co.lotteon.repository.custom;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {


    List<Product> findAll(BooleanBuilder builder);

    Page<Tuple> selectBestAllForList(int subCate);

    Page<Tuple> sortedProducts(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> sortedSearchProducts(PageRequestDTO pageRequestDTO, Pageable pageable);

    Page<Tuple> selectAllForListByRole(PageRequestDTO pageRequestDTO, Pageable pageable);

}
