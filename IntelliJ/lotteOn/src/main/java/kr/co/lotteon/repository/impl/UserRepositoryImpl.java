package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.entity.user.QUserDetails;
import kr.co.lotteon.repository.custom.UserRepositoryCustom;
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
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;
    private final QUserDetails qUserDetails = QUserDetails.userDetails;

    @Override
    public Page<Tuple> selectAllUser(PageRequestDTO pageRequestDTO, Pageable pageable) {
        List<Tuple> tupleList = queryFactory
                .select(qUser, qUserDetails)
                .from(qUserDetails)
                .join(qUser)
                .on(qUserDetails.user.uid.eq(qUser.uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qUser.regDate.desc())
                .fetch();

        long total = queryFactory
                .select(qUserDetails.count())
                .from(qUserDetails)
                .join(qUserDetails.user, qUser)
                .on(qUserDetails.user.uid.eq(qUser.uid))
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }
}
