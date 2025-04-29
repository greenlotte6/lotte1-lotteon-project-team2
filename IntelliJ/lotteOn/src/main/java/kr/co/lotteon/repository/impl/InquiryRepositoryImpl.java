package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.article.QInquiry;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.InquiryRepositoryCustom;
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
public class InquiryRepositoryImpl implements InquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QInquiry qInquiry = QInquiry.inquiry;
    private final QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {
        String prodNo = pageRequestDTO.getProdNo();

        BooleanExpression expression = qInquiry.prodNo.eq(prodNo);

        List<Tuple> tupleList = queryFactory
                .select(qInquiry, qUser.uid)
                .from(qInquiry)
                .join(qInquiry.user, qUser)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qInquiry.no.desc())
                .fetch();

        // 전체 개수 (for Page 객체 생성)
        long total = queryFactory
                .select(qInquiry.count())
                .from(qInquiry)
                .where(expression)
                .fetchOne();


        return new PageImpl<>(tupleList, pageable, total);
    }




}
