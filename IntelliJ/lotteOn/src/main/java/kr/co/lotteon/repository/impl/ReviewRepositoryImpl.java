package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.feedback.QReview;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.ReviewRepositoryCustom;
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
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct qProduct = QProduct.product;
    private final QReview qReview = QReview.review;
    private final QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        String prodNo = pageRequestDTO.getProdNo();
        String sortType = pageRequestDTO.getSortType();

        if (sortType == null) {
            sortType = "latest";
        }

        OrderSpecifier<?> orderSpecifier = null;
        switch (sortType) {
            case "highestRate":
                orderSpecifier = qReview.rating.desc();
                break;
            case "lowestRate":
                orderSpecifier = qReview.rating.asc();
                break;
            case "latest":
                orderSpecifier = qReview.wdate.desc();
                break;
        }

        BooleanExpression whereExpr = null;
        if (prodNo != null && !prodNo.isEmpty()) {
            whereExpr = qReview.product.prodNo.eq(prodNo);
        }

        List<Tuple> tupleList = queryFactory
                .select(qReview, qUser.uid)
                .from(qReview)
                .join(qReview.writer, qUser)
                .leftJoin(qProduct).on(qReview.product.prodNo.eq(qProduct.prodNo))
                .where(whereExpr)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        // 전체 개수 조회
        Long total = queryFactory
                .select(qReview.count())
                .from(qReview)
                .where(whereExpr)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total != null ? total : 0);
    }



}
