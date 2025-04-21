package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductDetail is a Querydsl query type for ProductDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDetail extends EntityPathBase<ProductDetail> {

    private static final long serialVersionUID = 704681502L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductDetail productDetail = new QProductDetail("productDetail");

    public final StringPath bizType = createString("bizType");

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final StringPath opt1 = createString("opt1");

    public final StringPath opt1Cont = createString("opt1Cont");

    public final StringPath opt2 = createString("opt2");

    public final StringPath opt2Cont = createString("opt2Cont");

    public final StringPath opt3 = createString("opt3");

    public final StringPath opt3Cont = createString("opt3Cont");

    public final StringPath opt4 = createString("opt4");

    public final StringPath opt4Cont = createString("opt4Cont");

    public final StringPath opt5 = createString("opt5");

    public final StringPath opt5Cont = createString("opt5Cont");

    public final StringPath opt6 = createString("opt6");

    public final StringPath opt6Cont = createString("opt6Cont");

    public final StringPath origin = createString("origin");

    public final StringPath prodState = createString("prodState");

    public final QProduct product;

    public final StringPath receiptType = createString("receiptType");

    public final StringPath taxFree = createString("taxFree");

    public QProductDetail(String variable) {
        this(ProductDetail.class, forVariable(variable), INITS);
    }

    public QProductDetail(Path<? extends ProductDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductDetail(PathMetadata metadata, PathInits inits) {
        this(ProductDetail.class, metadata, inits);
    }

    public QProductDetail(Class<? extends ProductDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

