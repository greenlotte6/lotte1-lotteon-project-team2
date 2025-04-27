package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.article.QNotice;
import kr.co.lotteon.entity.article.QRecruit;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.RecruitRepositoryCustom;
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
public class RecruitRepositoryImpl implements RecruitRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QRecruit qRecruit = QRecruit.recruit;
    private final QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllRecruit(PageRequestDTO pageRequestDTO, Pageable pageable) {
        String cate = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        // 전체
        if (cate == null || cate.equals("")) {
            List<Tuple> tupleList = queryFactory
                    .select(qRecruit, qUser.name)
                    .from(qRecruit)
                    .join(qUser)
                    .on(qRecruit.user.uid.eq(qUser.uid))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qRecruit.no.desc())
                    .fetch();

            long total = queryFactory
                    .select(qRecruit.count())
                    .from(qRecruit)
                    .join(qUser)
                    .on(qRecruit.user.uid.eq(qUser.uid))
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);

        } else {
            // 카테고리 별로 출력
            BooleanExpression expression = switch (cate) {
                case "번호" -> qRecruit.no.stringValue().contains(keyword);
                case "채용부서" -> qRecruit.department.contains(keyword);
                case "채용형태" -> qRecruit.employmentType.contains(keyword);
                case "제목" -> qRecruit.title.contains(keyword);
                default -> null;
            };

            List<Tuple> tupleList = queryFactory
                    .select(qRecruit, qUser.name)
                    .from(qRecruit)
                    .join(qUser)
                    .on(qRecruit.user.uid.eq(qUser.uid))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(expression)
                    .orderBy(qRecruit.no.desc())
                    .fetch();

            long total = queryFactory
                    .select(qRecruit.count())
                    .from(qRecruit)
                    .join(qUser)
                    .on(qRecruit.user.uid.eq(qUser.uid))
                    .where(expression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);


        }
    }
}
