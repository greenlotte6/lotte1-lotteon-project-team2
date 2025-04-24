package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.article.Notice;
import kr.co.lotteon.entity.article.QNotice;
import kr.co.lotteon.repository.custom.NoticeRepositoryCustom;
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
public class NoticeRepositoryImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QNotice qNotice = QNotice.notice;

    @Override
    public Page<Tuple> selectAllNotice(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String cate = pageRequestDTO.getSearchType();

        // 전체
        if(cate==null || cate.equals("전체") ){
            List<Tuple> tupleList = queryFactory
                    .select(qNotice, qNotice)
                    .from(qNotice)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qNotice.no.desc())
                    .fetch();

            long total = queryFactory
                    .select(qNotice.count())
                    .from(qNotice)
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);

        }else{
            // 카테고리 별로 출력
            BooleanExpression expression = qNotice.cate.eq(cate);

            List<Tuple> tupleList = queryFactory
                    .select(qNotice, qNotice)
                    .from(qNotice)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(expression)
                    .orderBy(qNotice.no.desc())
                    .fetch();

            long total = queryFactory
                    .select(qNotice.count())
                    .from(qNotice)
                    .where(expression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);

        }


    }
}
