package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.config.QVersion;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.VersionRepositoryCustom;
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
public class VersionRepositoryImpl implements VersionRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QVersion qVersion = QVersion.version1;
    private final QUser qUser = QUser.user;

    // 버전 목록
    @Override
    public Page<Tuple> selectAllForList(PageRequestDTO pageRequestDTO, Pageable pageable) {

        List<Tuple> tupleList = queryFactory
                .select(qVersion, qUser.uid)
                .from(qVersion)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qVersion.vno.desc())
                .fetch();

        long total = queryFactory
                .select(qVersion.count())
                .from(qVersion)
                .join(qUser).on(qVersion.user.uid.eq(qUser.uid))
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }
    
}
