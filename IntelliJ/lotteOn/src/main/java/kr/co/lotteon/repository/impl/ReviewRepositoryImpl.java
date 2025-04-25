package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
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
    private final QSeller qSeller = QSeller.seller;
    private final QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String prodNo = pageRequestDTO.getProdNo();
        BooleanExpression expression = prodNo != null ? qReview.product.prodNo.eq(prodNo) : null;

        List<Tuple> tupleList = queryFactory
                .select(qReview, qSeller.company, qUser.uid)
                .from(qReview)
                .join(qReview.writer, qUser)
                .leftJoin(qProduct).on(qReview.product.prodNo.eq(qProduct.prodNo))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qReview.rno.desc())
                .fetch();

        long total = queryFactory
                .select(qReview.count())
                .from(qReview)
                .where(expression)
                .fetchOne();

        return new PageImpl<>(tupleList, pageable, total);
    }




}
