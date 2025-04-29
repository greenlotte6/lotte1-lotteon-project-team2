package kr.co.lotteon.entity.feedback;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1151319394L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final StringPath content = createString("content");

    public final StringPath oNameImage1 = createString("oNameImage1");

    public final StringPath oNameImage2 = createString("oNameImage2");

    public final kr.co.lotteon.entity.product.QProduct product;

    public final NumberPath<java.math.BigDecimal> rating = createNumber("rating", java.math.BigDecimal.class);

    public final NumberPath<Integer> rno = createNumber("rno", Integer.class);

    public final StringPath sNameImage1 = createString("sNameImage1");

    public final StringPath SNameImage2 = createString("SNameImage2");

    public final DateTimePath<java.time.LocalDateTime> wdate = createDateTime("wdate", java.time.LocalDateTime.class);

    public final kr.co.lotteon.entity.user.QUser writer;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new kr.co.lotteon.entity.product.QProduct(forProperty("product"), inits.get("product")) : null;
        this.writer = inits.isInitialized("writer") ? new kr.co.lotteon.entity.user.QUser(forProperty("writer")) : null;
    }

}

