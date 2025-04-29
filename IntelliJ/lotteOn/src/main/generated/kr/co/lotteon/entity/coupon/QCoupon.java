package kr.co.lotteon.entity.coupon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 1213297387L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final StringPath benefit = createString("benefit");

    public final StringPath caution = createString("caution");

    public final NumberPath<Long> cno = createNumber("cno", Long.class);

    public final StringPath couponName = createString("couponName");

    public final StringPath couponType = createString("couponType");

    public final NumberPath<Integer> issueCount = createNumber("issueCount", Integer.class);

    public final StringPath issuedBy = createString("issuedBy");

    public final DatePath<java.time.LocalDate> regDate = createDate("regDate", java.time.LocalDate.class);

    public final StringPath state = createString("state");

    public final NumberPath<Integer> usedCount = createNumber("usedCount", Integer.class);

    public final kr.co.lotteon.entity.user.QUser user;

    public final NumberPath<Integer> validDaysAfterIssue = createNumber("validDaysAfterIssue", Integer.class);

    public final DatePath<java.time.LocalDate> validFrom = createDate("validFrom", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> validTo = createDate("validTo", java.time.LocalDate.class);

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new kr.co.lotteon.entity.user.QUser(forProperty("user")) : null;
    }

}

