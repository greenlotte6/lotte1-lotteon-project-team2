package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.article.QFaq;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.SellerRepositoryCustom;
import kr.co.lotteon.repository.seller.SellerRepository;
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
public class SellerRepositoryImpl implements SellerRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QSeller qSeller = QSeller.seller;
    private final QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectAllSellerByType(PageRequestDTO pageRequestDTO, Pageable pageable) {
        List<Tuple> tupleList = queryFactory
                .select(qSeller, qUser)
                .from(qSeller)
                .join(qSeller.user, qUser)
                .on(qSeller.user.uid.eq(qUser.uid))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qSeller.sno.desc())
                .fetch();

        long total = queryFactory
                .select(qSeller.count())
                .from(qSeller)
                .join(qSeller.user, qUser)
                .on(qSeller.user.uid.eq(qUser.uid))
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectAllSellerByTypeAndKeyword(PageRequestDTO pageRequestDTO, Pageable pageable) {

        BooleanExpression expression = null;
        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getSearchType();

        switch (type) {
            case "상호명": expression = qSeller.company.contains(keyword); break;
            case "대표자": expression = qSeller.ceo.contains(keyword); break;
            case "사업자등록번호" : expression = qSeller.bizRegNo.contains(keyword); break;
            case "연락처" : expression = qSeller.user.hp.contains(keyword); break;
            default: break;
        }
        
        List<Tuple> tupleList = queryFactory
                .select(qSeller, qUser)
                .from(qSeller)
                .join(qSeller.user, qUser)
                .on(qSeller.user.uid.eq(qUser.uid))
                .where(expression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qSeller.sno.desc())
                .fetch();

        long total = queryFactory
                .select(qSeller.count())
                .from(qSeller)
                .join(qSeller.user, qUser)
                .on(qSeller.user.uid.eq(qUser.uid))
                .where(expression)
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }
}
