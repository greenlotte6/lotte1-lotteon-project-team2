package kr.co.lotteon.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.order.QOrder;
import kr.co.lotteon.entity.order.QOrderItem;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.repository.custom.OrderRepositoryCustom;
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
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QOrder qOrder = QOrder.order;
    private final QOrderItem qOrderItem = QOrderItem.orderItem;
    private final QProduct qProduct = QProduct.product;
    private final QSeller qSeller = QSeller.seller;

    // 판매자 별 매출 통계
    @Override
    public Page<Tuple> selectAllSales(PageRequestDTO pageRequestDTO, Pageable pageable) {


        String searchType = pageRequestDTO.getSearchType();
        String keyword = pageRequestDTO.getKeyword();

        List<Tuple> tupleList = queryFactory
                .select(qSeller, qOrderItem)
                .from(qOrderItem)
                .join(qOrderItem.order, qOrder)
                .on(qOrder.orderNo.eq(qOrderItem.order.orderNo))
                .join(qOrderItem.product, qProduct)
                .on(qOrderItem.product.prodNo.eq(qProduct.prodNo))
                .join(qProduct.seller, qSeller)
                .on(qSeller.sno.eq(qProduct.seller.sno))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qSeller.sno.desc())
                .fetch();

        long total = queryFactory
                .select(qOrderItem.count())
                .from(qOrderItem)
                .join(qOrderItem.order, qOrder)
                .on(qOrder.orderNo.eq(qOrderItem.order.orderNo))
                .join(qOrderItem.product, qProduct)
                .on(qOrderItem.product.prodNo.eq(qProduct.prodNo))
                .join(qProduct.seller, qSeller)
                .on(qSeller.sno.eq(qProduct.seller.sno))
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);

        // 판매자 회사 별 매출 정보


    }
}
