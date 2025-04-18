package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.review.QReview;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QSeller qSeller = QSeller.seller;
    private final QReview qReview = QReview.review;

    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String cate = pageRequestDTO.getCate();

        BooleanExpression expression = QProduct.product.cate.eq(cate);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.cname)
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.id.eq(qSeller.id))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProduct.no.desc())
                .fetch();

        long total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.id.eq(qSeller.id))
                .where(expression)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectBestAllForList(String cate) {

        BooleanExpression expression = QProduct.product.cate.eq(cate);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.cname)
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.id.eq(qSeller.id))
                .where(expression)
                .orderBy(qProduct.salesCount.desc()) // 판매량 기준으로 내림차순 정렬
                .limit(10) // 상위 10개만 선택
                .fetch();

        return new PageImpl<>(tupleList);
    }


}
