package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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

        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company, qSeller.rank, qProductImage.sNameList)
                .from(qProduct)
                .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                .leftJoin(qProductImage).on(qProductImage.product.prodNo.eq(qProduct.prodNo))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProduct.regDate.asc())
                .fetch();

        long total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                .leftJoin(qProductImage).on(qProductImage.product.prodNo.eq(qProduct.prodNo))
                .where(expression)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total);
    }

    // 카테고리별 베스트
    @Override
    public Page<Tuple> selectBestAllForList(int subCateNo) {
        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo);

        //  일관된 시간 생성 방식 적용
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        BooleanExpression recentProduct = qProduct.regDate.after(threeMonthsAgo);

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company, qProductImage.sNameList)
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
        String period = pageRequestDTO.getPeriod();

        OrderSpecifier<?> orderSpecifier = qProduct.regDate.desc(); // 기본 정렬: 최신 등록순 (sortType이 null일 경우)

        NumberExpression<Integer> discountedPrice = qProduct.prodPrice
                .multiply(
                        Expressions
                                .asNumber(1)
                                .subtract(
                                        qProduct.prodDiscount.divide(100)
                                )
                )
                .floor()
                .castToNum(Integer.class);

        if (sortType != null) {
            switch (sortType) {
                case "mostSales" -> orderSpecifier = qProduct.prodSold.desc();
                case "lowPrice" -> orderSpecifier = discountedPrice.asc();
                case "highPrice" -> orderSpecifier =  discountedPrice.desc();
                case "highRating" -> orderSpecifier = qProduct.ratingAvg.desc();
                case "manyReviews" -> orderSpecifier = qProduct.reviewCount.desc();
                case "latest" -> orderSpecifier = qProduct.regDate.desc();
            }
        }

        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo);

        if ((sortType != null && (sortType.equals("mostSales") || sortType.equals("manyReviews"))) && period != null) {
            LocalDate now = LocalDate.now();
            LocalDate startDate = switch (period) {
                case "1year" -> now.minusYears(1);
                case "6month" -> now.minusMonths(6);
                case "1month" -> now.minusMonths(1);
                default -> null;
            };

            if (startDate != null) {
                expression = expression.and(qProduct.regDate.after(startDate.atStartOfDay()));
            }
        }

        List<Tuple> tupleList = queryFactory
                .select(qProduct, qSeller.company, qSeller.rank, qProductImage.sNameList)
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
                .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                .where(expression)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total);
    }

    // 관리자 상품 목록 조회(관리자)
    @Override
    public Page<Tuple> selectAllForListByRole(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String cate = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        if(cate == null || cate.equals("")) {
            List<Tuple> tupleList = queryFactory
                    .select(qProduct, qSeller.company, qProductImage)
                    .from(qProduct)
                    .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                    .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qProduct.regDate.desc()) // 정렬 조건
                    .fetch();

            long total = queryFactory
                    .select(qProduct.count())
                    .from(qProduct)
                    .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                    .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);
        }else{

            BooleanExpression expression = switch (cate) {
                case "상품명" -> qProduct.prodName.contains(keyword);
                case "상품번호" -> qProduct.prodNo.contains(keyword);
                case "판매자" -> qProduct.company.contains(keyword);
                default -> null;
            };

            List<Tuple> tupleList = queryFactory
                    .select(qProduct, qSeller.company, qProductImage)
                    .from(qProduct)
                    .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                    .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                    .where(expression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qProduct.regDate.desc()) // 정렬 조건
                    .fetch();

            long total = queryFactory
                    .select(qProduct.count())
                    .from(qProduct)
                    .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                    .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                    .where(expression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);
        }


    }

}
