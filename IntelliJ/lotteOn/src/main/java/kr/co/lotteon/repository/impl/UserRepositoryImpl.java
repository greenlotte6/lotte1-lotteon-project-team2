package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.point.QPoint;
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
    private final QPoint qPoint = QPoint.point1;

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

    @Override
    public Page<Tuple> selectAllPoint(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String sortType = pageRequestDTO.getSortType();
        String keyword = pageRequestDTO.getKeyword();

        if(sortType == null){
            List<Tuple> tupleList = queryFactory
                    .select(qUser, qPoint)
                    .from(qPoint)
                    .join(qUser)
                    .on(qPoint.user.uid.eq(qUser.uid))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qPoint.pointNo.desc())
                    .fetch();

            long total = queryFactory
                    .select(qPoint.count())
                    .from(qPoint)
                    .join(qPoint.user, qUser)
                    .on(qPoint.user.uid.eq(qUser.uid))
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);
        }else{

            BooleanExpression expression = switch (sortType) {
                case "아이디" -> qUser.uid.contains(keyword);
                case "이름" -> qUser.name.contains(keyword);
                case "지급내용" -> qPoint.pointDesc.contains(keyword);
                default -> null;
            };

            List<Tuple> tupleList = queryFactory
                    .select(qUser, qPoint)
                    .from(qPoint)
                    .join(qUser)
                    .on(qPoint.user.uid.eq(qUser.uid))
                    .where(expression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qPoint.pointNo.desc())
                    .fetch();

            long total = queryFactory
                    .select(qPoint.count())
                    .from(qPoint)
                    .join(qPoint.user, qUser)
                    .on(qPoint.user.uid.eq(qUser.uid))
                    .where(expression)
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);




        }


    }
}
