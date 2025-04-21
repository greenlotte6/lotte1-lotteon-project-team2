package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.product.QProductImage;
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
    private final QProductImage qProductImage = QProductImage.productImage;


    // 상품 목록
    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        int subCateNo = pageRequestDTO.getSubCateNo();
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3).withHour(0).withMinute(0).withSecond(0).withNano(0);

        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo)
                .and(qProduct.regDate.after(threeMonthsAgo)); // ✅ regDate 조건 추가

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company, qProductImage.sNameThumb3)
                .from(qProduct)
                .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProduct.regDate.asc()) // 정렬 조건
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

    // 카테고리별 베스트
    @Override
    public Page<Tuple> selectBestAllForList(int subCateNo) {
        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo);

        // ✅ 일관된 시간 생성 방식 적용
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        BooleanExpression recentProduct = qProduct.regDate.after(threeMonthsAgo);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company, qProductImage.sNameThumb3)
                .from(qProduct)
                .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                .where(expression.and(recentProduct))
                .orderBy(qProduct.prodSold.desc())
                .limit(10)
                .fetch();

        return new PageImpl<>(tupleList);
    }


    // 상품 목록 정렬
    @Override
    public Page<Tuple> sortedProducts(PageRequestDTO pageRequestDTO, Pageable pageable) {
        int subCateNo = pageRequestDTO.getSubCateNo();
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

        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company, qProductImage.sNameThumb3)
                .from(qProduct)
                .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
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
