package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.coupon.QCoupon;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.CouponRepositoryCustom;
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
public class CouponRepositoryImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCoupon qCoupon = QCoupon.coupon;
    private final QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllCoupon(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String role = pageRequestDTO.getRole();
        String uid = pageRequestDTO.getUid();


        if(pageRequestDTO.getSearchType().equals("전체")) {

            String keyword = pageRequestDTO.getKeyword();
            if(keyword != null) {
                BooleanExpression expression = qCoupon.couponName.containsIgnoreCase(keyword);

                if(role.contains("SELLER")){
                    expression = expression.and(qUser.uid.eq(uid));
                }

                List<Tuple> tupleList = queryFactory
                        .select(qCoupon, qCoupon)
                        .from(qCoupon)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .where(expression)
                        .orderBy(qCoupon.cno.desc()) // 정렬 조건
                        .fetch();

                long total = queryFactory
                        .select(qCoupon.count())
                        .from(qCoupon)
                        .where(expression)
                        .fetchOne();

                return new PageImpl<>(tupleList, pageable, total);
            }else{
                List<Tuple> tupleList = queryFactory
                        .select(qCoupon, qCoupon)
                        .from(qCoupon)
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .orderBy(qCoupon.cno.desc()) // 정렬 조건
                        .fetch();

                long total = queryFactory
                        .select(qCoupon.count())
                        .from(qCoupon)
                        .fetchOne();

                return new PageImpl<>(tupleList, pageable, total);

            }

        }else{

            String couponType = pageRequestDTO.getSearchType();
            BooleanExpression expression; // = qCoupon.couponType.eq(couponType);

            if(couponType.equals("쿠폰번호")) {
                expression = qCoupon.cno.like("%"+pageRequestDTO.getKeyword()+"%");
            }else if(couponType.equals("쿠폰명")){
                expression = qCoupon.couponName.containsIgnoreCase(pageRequestDTO.getKeyword());
            }else{
                expression = qCoupon.issuedBy.contains(pageRequestDTO.getKeyword());
            }

            if(role.contains("SELLER")){
                expression = expression.and(qUser.uid.eq(uid));
            }

            String keyword = pageRequestDTO.getKeyword();

            List<Tuple> tupleList = queryFactory
                    .select(qCoupon, qCoupon)
                    .from(qCoupon)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .where(expression)
                    .orderBy(qCoupon.cno.desc()) // 정렬 조건
                    .fetch();

            long total = queryFactory
                    .select(qCoupon.count())
                    .from(qCoupon)
                    .where(expression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);






        }


    }
}
