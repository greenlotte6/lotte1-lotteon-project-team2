package kr.co.lotteon.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lotteon.dto.page.PageRequestDTO;
import kr.co.lotteon.entity.order.QOrder;
import kr.co.lotteon.entity.order.QOrderItem;
import kr.co.lotteon.entity.product.QProduct;
import kr.co.lotteon.entity.product.QProductImage;
import kr.co.lotteon.entity.seller.QSeller;
import kr.co.lotteon.entity.user.QUser;
import kr.co.lotteon.repository.custom.OrderRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QOrder qOrder = QOrder.order;
    private final QOrderItem qOrderItem = QOrderItem.orderItem;
    private final QProduct qProduct = QProduct.product;
    private final QProductImage qProductImage = QProductImage.productImage;
    private final QSeller qSeller = QSeller.seller;
    private final QUser qUser = QUser.user;

    // 판매자 별 매출 통계
    @Override
    public Page<Tuple> selectAllSales(PageRequestDTO pageRequestDTO, Pageable pageable) {

        String sort = pageRequestDTO.getSearchType();

        if(sort == null || sort.equals("일별")){
            List<Tuple> tupleList = queryFactory
                    .select(qSeller, Expressions.numberTemplate(Long.class,
                                    "sum(round({0} * (1 - ({1} / 100.0)) * {2}))",
                                    qOrderItem.itemPrice, qOrderItem.itemDiscount, qOrderItem.itemCount
                            ))
                    .from(qOrderItem)
                    .join(qOrderItem.order, qOrder)
                    .join(qOrderItem.product, qProduct)
                    .join(qProduct.seller, qSeller)
                    .groupBy(qSeller.sno) // 판매자 기준으로 그룹핑
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qSeller.sno.desc())
                    .fetch();

            long total = queryFactory
                    .select(qSeller.countDistinct())
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

        }else{

            LocalDateTime term = LocalDateTime.now();
            if(sort.equals("주간")){
                term = term.minusDays(7);
            }else{
                term = term.minusMonths(1);
            }

            BooleanExpression expression = qOrder.orderDate.after(term);

            List<Tuple> tupleList = queryFactory
                    .select(qSeller, Expressions.numberTemplate(Long.class,
                            "sum(round({0} * (1 - ({1} / 100.0)) * {2}))",
                            qOrderItem.itemPrice, qOrderItem.itemDiscount, qOrderItem.itemCount
                    ))
                    .from(qOrderItem)
                    .join(qOrderItem.order, qOrder)
                    .join(qOrderItem.product, qProduct)
                    .join(qProduct.seller, qSeller)
                    .where(expression)
                    .groupBy(qSeller.sno) // 판매자 기준으로 그룹핑
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qSeller.sno.desc())
                    .fetch();

            long total = queryFactory
                    .select(qSeller.countDistinct())
                    .from(qOrderItem)
                    .join(qOrderItem.order, qOrder)
                    .on(qOrder.orderNo.eq(qOrderItem.order.orderNo))
                    .join(qOrderItem.product, qProduct)
                    .on(qOrderItem.product.prodNo.eq(qProduct.prodNo))
                    .join(qProduct.seller, qSeller)
                    .on(qSeller.sno.eq(qProduct.seller.sno))
                    .where(expression)
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);
        }



    }

    @Override
    public Page<Tuple> orderInfoPaging(PageRequestDTO pageRequestDTO, Pageable pageable, String uid) {

        BooleanExpression booleanExpression = qOrder.user.uid.eq(uid);

        List<Tuple> tupleList = queryFactory
                .select(qOrderItem, qOrder, qOrder.orderDate, qOrderItem.orderStatus , qProductImage.sNameThumb3, qSeller, qUser)
                .from(qOrderItem)
                .join(qOrderItem.order, qOrder)
                .join(qOrderItem.product, qProduct)
                .join(qProduct.seller, qSeller)
                .join(qOrder.user, qUser)
                .where(booleanExpression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrder.orderNo.desc())
                .fetch();

        long total = queryFactory
                .select(qOrderItem.count())
                .from(qOrderItem)
                .join(qOrderItem.order, qOrder)
                .join(qOrderItem.product, qProduct)
                .join(qProduct.seller, qSeller)
                .join(qOrder.user, qUser)
                .where(booleanExpression)
                .fetchOne();

        log.info("total: {}", total);
        log.info("tupleList: {}", tupleList);

        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> orderInfoPagingSearch(PageRequestDTO pageRequestDTO, Pageable pageable, String uid) {

        BooleanExpression booleanExpression = qOrder.user.uid.eq(uid);


        // 1. 키워드 검색
        String keyword = pageRequestDTO.getKeyword();
        String searchType = pageRequestDTO.getSearchType();

        LocalDate startDate = pageRequestDTO.getStart();
        LocalDate endDate =  pageRequestDTO.getEnd();

        if(searchType.equals("전체")){
            searchType = null;
        }

        if (searchType != null && !searchType.isEmpty()) {
            booleanExpression = booleanExpression.and(qOrderItem.orderStatus.eq(searchType));
        }

        if (keyword != null && !keyword.isEmpty()) {
            booleanExpression = booleanExpression.and(qOrderItem.product.prodName.like("%" + keyword + "%"));
        }

        if (startDate != null && endDate != null) {
            booleanExpression = booleanExpression.and(qOrder.orderDate.between(startDate.atStartOfDay(), endDate.atStartOfDay()));
        }


        List<Tuple> tupleList = queryFactory
                .select(qOrderItem, qOrder, qOrder.orderDate, qOrderItem.orderStatus , qProductImage.sNameThumb3, qSeller, qUser)
                .from(qOrderItem)
                .join(qOrderItem.order, qOrder)
                .join(qOrderItem.product, qProduct)
                .join(qProduct.seller, qSeller)
                .join(qOrder.user, qUser)
                .where(booleanExpression)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrder.orderNo.desc())
                .fetch();

        long total = queryFactory
                .select(qOrderItem.count())
                .from(qOrderItem)
                .join(qOrderItem.order, qOrder)
                .join(qOrderItem.product, qProduct)
                .join(qProduct.seller, qSeller)
                .join(qOrder.user, qUser)
                .where(booleanExpression)
                .fetchOne();

        log.info("total: {}", total);


        return new PageImpl<>(tupleList, pageable, total);
    }

    @Override
    public Page<Tuple> selectAllOrder(PageRequestDTO pageRequestDTO, Pageable pageable) {

        BooleanExpression expression = null;

        String role = pageRequestDTO.getRole();
        String uid = pageRequestDTO.getUid();

        String keyword = pageRequestDTO.getKeyword();
        String type = pageRequestDTO.getSearchType();

        if(role.contains("SELLER")){
            expression = qSeller.user.uid.eq(uid);
        }

        if(type == null || type.equals("")){

            List<Tuple> tupleList = queryFactory
                    .select(qOrder, qOrder.orderDate)
                    .from(qOrder)
                    .join(qOrder.orderItems, qOrderItem)
                    .join(qOrderItem.product, qProduct)
                    .join(qProduct.seller, qSeller)
                    .where(expression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qOrder.orderNo.desc())
                    .fetch();

            long total = queryFactory
                    .select(qOrder.count())
                    .from(qOrder)
                    .join(qOrder.orderItems, qOrderItem)
                    .join(qOrderItem.product, qProduct)
                    .join(qProduct.seller, qSeller)
                    .where(expression)
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);



            /*
            List<Tuple> tupleList = queryFactory
                    .select(qOrder, qOrder.orderDate)
                    .from(qOrder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qOrder.orderNo.desc())
                    .fetch();

            long total = queryFactory
                    .select(qOrder.count())
                    .from(qOrder)
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);
*/
        }else{

            BooleanExpression booleanExpression = null;

            switch (type){
                case "주문번호" : {
                    booleanExpression = qOrder.orderNo.like("%"+keyword+"%");
                    break;
                }
                case "주문자" : {
                    booleanExpression = qOrder.user.uid.like("%"+keyword+"%");
                    break;
                }
                case "주문자명" : {
                    booleanExpression = qOrder.user.name.like("%"+keyword+"%");
                    break;
                }
                default : {
                    break;
                }
            }

            if(booleanExpression != null && role.contains("SELLER")){
                booleanExpression = booleanExpression.and(qSeller.user.uid.eq(uid));
            }


            List<Tuple> tupleList = queryFactory
                    .select(qOrder, qOrder.orderDate)
                    .from(qOrder)
                    .join(qOrder.orderItems, qOrderItem)
                    .join(qOrderItem.product, qProduct)
                    .join(qProduct.seller, qSeller)
                    .where(booleanExpression)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .orderBy(qOrder.orderNo.desc())
                    .fetch();

            long total = queryFactory
                    .select(qOrder.count())
                    .from(qOrder)
                    .join(qOrder.orderItems, qOrderItem)
                    .join(qOrderItem.product, qProduct)
                    .join(qProduct.seller, qSeller)
                    .where(booleanExpression)
                    .fetchOne();

            log.info("total: {}", total);
            log.info("tupleList: {}", tupleList);

            return new PageImpl<>(tupleList, pageable, total);

        }


    }
}
