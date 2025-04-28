package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.article.QFaq;
import kr.co.lotteon.entity.article.QRecruit;
import kr.co.lotteon.repository.custom.FaqRepositoryCustom;
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
public class FaqRepositoryImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QFaq qFaq = QFaq.faq;

    @Override
    public Page<Tuple> selectAllFaq(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<Tuple> tupleList = queryFactory
                .select(qFaq, qFaq.title)
                .from(qFaq)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qFaq.no.desc())
                .fetch();

        long total = queryFactory
                .select(qFaq.count())
                .from(qFaq)
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectAllFaqByType(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String type = pageRequestDTO.getSearchType();
        BooleanExpression expression = qFaq.cateV2.eq(type);

        List<Tuple> tupleList = queryFactory
                .select(qFaq, qFaq.title)
                .from(qFaq)
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qFaq.no.desc())
                .fetch();

        long total = queryFactory
                .select(qFaq.count())
                .from(qFaq)
                .where(expression)
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }
}
