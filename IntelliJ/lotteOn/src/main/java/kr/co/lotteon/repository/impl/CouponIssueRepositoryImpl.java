package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.coupon.QCoupon;
import kr.co.lotteon.entity.coupon.QCouponIssue;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.coupon.CouponIssueRepository;
import kr.co.lotteon.repository.custom.CouponIssueRepositoryCustom;
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
public class CouponIssueRepositoryImpl implements CouponIssueRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QCouponIssue qCouponIssue = QCouponIssue.couponIssue;
    private final QUser qUser = QUser.user;
    private final QCoupon qCoupon = QCoupon.coupon;


    @Override
    public Page<Tuple> selectAllCouponIssue(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String type = pageRequestDTO.getSearchType();


        // 기존 페이징 기능
        if (type == null || type.equals("")) {

            BooleanExpression expression = null;

            String role = pageRequestDTO.getRole();
            String uid = pageRequestDTO.getUid();

            if(role.contains("SELLER")){
                expression = qCoupon.user.uid.eq(uid);
            }


            List<Tuple> tupleList = queryFactory
                    .select(qCouponIssue, qCoupon, qUser.uid)
                    .from(qCouponIssue)
                    .join(qCoupon).on(qCoupon.cno.eq(qCouponIssue.coupon.cno))
                    .join(qUser).on(qCouponIssue.user.uid.eq(qUser.uid))
                    .where(expression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qCoupon.cno.desc()) // 정렬 조건
                    .fetch();

            long total = queryFactory
                    .select(qCoupon.count())
                    .from(qCouponIssue)
                    .join(qCoupon).on(qCoupon.cno.eq(qCouponIssue.coupon.cno))
                    .join(qUser).on(qCouponIssue.user.uid.eq(qUser.uid))
                    .where(expression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);
        } else {
            // 검색 페이징 기능
            BooleanExpression booleanExpression;
            if (type.equals("발급번호")) {
                booleanExpression = qCouponIssue.issueNo.like("%" + pageRequestDTO.getKeyword() + "%");
            } else if (type.equals("쿠폰번호")) {
                booleanExpression = qCoupon.cno.like("%" + pageRequestDTO.getKeyword() + "%");
            } else if (type.equals("쿠폰명")) {
                booleanExpression = qCoupon.couponName.contains(pageRequestDTO.getKeyword());
            } else {
                booleanExpression = qUser.uid.contains(pageRequestDTO.getKeyword());
            }

            String role = pageRequestDTO.getRole();
            String uid = pageRequestDTO.getUid();

            if(role.contains("SELLER")){
                booleanExpression = booleanExpression.and(qCoupon.user.uid.eq(uid));
            }

            List<Tuple> tupleList = queryFactory
                    .select(qCouponIssue, qCoupon, qUser.uid)
                    .from(qCouponIssue)
                    .join(qCoupon).on(qCoupon.cno.eq(qCouponIssue.coupon.cno))
                    .join(qUser).on(qCouponIssue.user.uid.eq(qUser.uid))
                    .where(booleanExpression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qCoupon.cno.desc()) // 정렬 조건
                    .fetch();

            long total = queryFactory
                    .select(qCoupon.count())
                    .from(qCouponIssue)
                    .join(qCoupon).on(qCoupon.cno.eq(qCouponIssue.coupon.cno))
                    .join(qUser).on(qCouponIssue.user.uid.eq(qUser.uid))
                    .where(booleanExpression)
                    .fetchOne();

            return new PageImpl<>(tupleList, pageable, total);


        }
    }
}
