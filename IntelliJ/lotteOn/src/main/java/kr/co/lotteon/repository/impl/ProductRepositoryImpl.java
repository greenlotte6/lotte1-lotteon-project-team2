package kr.co.lotteon.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.product.Product;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.product.QProductImage;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QSeller qSeller = QSeller.seller;
    private final QProductImage qProductImage = QProductImage.productImage;


    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findAll(BooleanBuilder builder) {
        JPAQuery<Product> query = new JPAQuery<>(em);
        QProduct product = QProduct.product;
        return query.from(product)
                .where(builder)
                .fetch();
    }


    // 카테고리별 베스트
    @Override
    public Page<Tuple> selectBestAllForList(int subCateNo) {

        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo)
                .and(qProduct.state.eq("판매"));


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

        OrderSpecifier<?> orderSpecifier = qProduct.regDate.desc();

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

        BooleanExpression expression = qProduct.subCategory.subCateNo.eq(subCateNo)
                .and(qProduct.state.eq("판매"));


        if ((sortType != null && (sortType.equals("mostSales") || sortType.equals("manyReviews"))) && period != null) {
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
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


    // 상품 검색 목록 정렬
    @Override
    public Page<Tuple> sortedSearchProducts(PageRequestDTO pageRequestDTO, Pageable pageable) {
        String sortType = pageRequestDTO.getSortType();
        String period = pageRequestDTO.getPeriod();

        // 1차 검색 필드
        String keyword = pageRequestDTO.getKeyword();

        // 2차 검색 필드
        String searchType = pageRequestDTO.getSearchType();
        String subKeyword = pageRequestDTO.getSubKeyword();
        int minPrice = pageRequestDTO.getMinPrice();
        int maxPrice = pageRequestDTO.getMaxPrice();

        OrderSpecifier<?> orderSpecifier = qProduct.regDate.desc();

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

        BooleanExpression expression = qProduct.state.eq("판매");


        if ((sortType != null && (sortType.equals("mostSales") || sortType.equals("manyReviews"))) && period != null) {
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Seoul"));
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


        // 1차 검색 조건
        if (keyword != null && !keyword.trim().isEmpty()) {
            BooleanBuilder keywordBooleanBuilder = new BooleanBuilder();

            keywordBooleanBuilder.or(qProduct.prodName.containsIgnoreCase(keyword));
            keywordBooleanBuilder.or(qProduct.prodBrand.containsIgnoreCase(keyword));
            keywordBooleanBuilder.or(qProduct.subCategory.subCategoryName.containsIgnoreCase(keyword));

            expression = expression.and(keywordBooleanBuilder);
        }


        // 2차 검색 조건
        if (searchType != null && !searchType.trim().isEmpty()) {
            switch (searchType) {
                case "상품명":
                    if (subKeyword != null && !subKeyword.trim().isEmpty()) {
                        expression = expression.and(qProduct.prodName.containsIgnoreCase(subKeyword));
                    }
                    break;
                case "브랜드":
                    if (subKeyword != null && !subKeyword.trim().isEmpty()) {
                        expression = expression.and(qProduct.prodBrand.containsIgnoreCase(subKeyword));
                    }
                    break;
                case "가격":
                    if (minPrice > 0) {
                        expression = expression.and(discountedPrice.goe(minPrice));
                    }
                    if (maxPrice > 0) {
                        expression = expression.and(discountedPrice.loe(maxPrice));
                    }
                    Predicate subKeywordInPriceContext = createTextSearchCondition(subKeyword, qProduct);
                    if (subKeywordInPriceContext != null) {
                        expression = expression.and(subKeywordInPriceContext);
                    }
                    break;
            }
        } else { // searchType이 없는 경우
            Predicate generalSubKeywordCondition = createTextSearchCondition(subKeyword, qProduct);
            if (generalSubKeywordCondition != null) {
                expression = expression.and(generalSubKeywordCondition);
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

        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }


    // 2차 검색 조건 메서드
    private Predicate createTextSearchCondition(String term, QProduct qProduct) {
        if (term == null || term.trim().isEmpty()) {
            return null; // 검색어가 없으면 조건을 추가하지 않음
        }

        BooleanBuilder builder = new BooleanBuilder();
        builder.or(qProduct.prodName.containsIgnoreCase(term));
        builder.or(qProduct.prodBrand.containsIgnoreCase(term));
        builder.or(qProduct.subCategory.subCategoryName.containsIgnoreCase(term));

        return builder;
    }



    // 관리자 상품 목록 조회(관리자)
    @Override
    public Page<Tuple> selectAllForListByRole(PageRequestDTO pageRequestDTO, Pageable pageable) {

        BooleanExpression booleanExpression = qProduct.state.eq("판매");

        String role = pageRequestDTO.getRole();
        String uid = pageRequestDTO.getUid();

        if(role.contains("SELLER")){
            booleanExpression = booleanExpression.and(qSeller.user.uid.eq(uid));
        }

        String cate = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        if(cate == null || cate.equals("")) {
            List<Tuple> tupleList = queryFactory
                    .select(qProduct, qSeller.company, qProductImage)
                    .from(qProduct)
                    .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                    .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                    .where(booleanExpression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qProduct.regDate.desc()) // 정렬 조건
                    .fetch();

            long total = queryFactory
                    .select(qProduct.count())
                    .from(qProduct)
                    .join(qSeller).on(qProduct.seller.sno.eq(qSeller.sno))
                    .leftJoin(qProductImage).on(qProductImage.product.eq(qProduct))
                    .where(booleanExpression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);
        }else{

            BooleanExpression baseCondition = qProduct.state.eq("판매");

            BooleanExpression expression = switch (cate) {
                case "상품명" -> qProduct.prodName.contains(keyword);
                case "상품번호" -> qProduct.prodNo.contains(keyword);
                case "판매자" -> qProduct.company.contains(keyword);
                default -> null;
            };

            if (expression != null) {
                expression = expression.and(baseCondition);
            } else {
                expression = baseCondition;
            }

            if(role.contains("SELLER")){
                expression = expression.and(qSeller.user.uid.eq(uid));
            }

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
