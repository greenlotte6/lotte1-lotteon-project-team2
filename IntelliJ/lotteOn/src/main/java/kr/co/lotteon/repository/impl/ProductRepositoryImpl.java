package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QSeller qSeller = QSeller.seller;

    // 상품 목록
    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        String cate = pageRequestDTO.getCate();

        BooleanExpression expression = qProduct.mainCategory.mainCategoryName.contains(cate);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company)
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.sno.eq(qSeller.sno))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProduct.prodNo.desc())
                .fetch();

        long total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.sno.eq(qSeller.sno))
                .where(expression)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total);
    }

    // 베스트 상품
    @Override
    public Page<Tuple> selectBestAllForList(String cate) {
        BooleanExpression expression = qProduct.mainCategory.mainCategoryName.contains(cate);

        // 오늘로부터 3개월 전 날짜 계산
        LocalDateTime threeMonthsAgo = LocalDate.now().minusMonths(3).atStartOfDay();

        // 상품 등록일 조건
        BooleanExpression recentProduct = qProduct.regDate.after(threeMonthsAgo);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company)
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.sno.eq(qSeller.sno))
                .where(expression.and(recentProduct))
                .orderBy(qProduct.prodSold.desc())
                .limit(10)
                .fetch();

        return new PageImpl<>(tupleList);
    }

    // 상품 목록 정렬
    @Override
    public Page<Tuple> sortedProducts(PageRequestDTO pageRequestDTO, Pageable pageable) {
        String cate = pageRequestDTO.getCate();
        String sortType = pageRequestDTO.getSortType();

        OrderSpecifier<?> orderSpecifier = null;

        switch (sortType) {
            case "mostSales" -> orderSpecifier = qProduct.prodSold.desc(); // 판매많은순
            case "lowPrice" -> orderSpecifier = qProduct.prodPrice.asc();   // 낮은가격순
            case "highPrice" -> orderSpecifier = qProduct.prodPrice.desc(); // 높은가격순
            case "highRating" -> orderSpecifier = qProduct.ratingAvg.asc(); // 평점높은순
            case "mostReviews" -> orderSpecifier = qProduct.reviewCount.desc(); // 후기많은순
            case "latest" -> orderSpecifier = qProduct.regDate.desc();  // 최신등록순
        }

        BooleanExpression expression = qProduct.mainCategory.mainCategoryName.contains(cate);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company)
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.sno.eq(qSeller.sno))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();


        long total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .join(qSeller)
                .on(qProduct.seller.sno.eq(qSeller.sno))
                .where(expression)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total);
    }

}
