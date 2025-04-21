package kr.co.lotteon.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCouponIssue is a Querydsl query type for CouponIssue
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponIssue extends EntityPathBase<CouponIssue> {

    private static final long serialVersionUID = 1610834094L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCouponIssue couponIssue = new QCouponIssue("couponIssue");

    public final QCoupon coupon;

    public final NumberPath<Integer> issueNo = createNumber("issueNo", Integer.class);

    public final StringPath state = createString("state");

    public final DateTimePath<java.time.LocalDateTime> usedDate = createDateTime("usedDate", java.time.LocalDateTime.class);

    public final kr.co.lotteon.entity.user.QUser user;

    public QCouponIssue(String variable) {
        this(CouponIssue.class, forVariable(variable), INITS);
    }

    public QCouponIssue(Path<? extends CouponIssue> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCouponIssue(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCouponIssue(PathMetadata metadata, PathInits inits) {
        this(CouponIssue.class, metadata, inits);
    }

    public QCouponIssue(Class<? extends CouponIssue> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.coupon = inits.isInitialized("coupon") ? new QCoupon(forProperty("coupon"), inits.get("coupon")) : null;
        this.user = inits.isInitialized("user") ? new kr.co.lotteon.entity.user.QUser(forProperty("user")) : null;
    }

}

