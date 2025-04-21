package kr.co.lotteon.entity.feedback;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QExchange is a Querydsl query type for Exchange
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QExchange extends EntityPathBase<Exchange> {

    private static final long serialVersionUID = -360374295L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QExchange exchange = new QExchange("exchange");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> eno = createNumber("eno", Integer.class);

    public final StringPath oName = createString("oName");

    public final StringPath sName = createString("sName");

    public final StringPath type = createString("type");

    public final kr.co.lotteon.entity.user.QUser user;

    public QExchange(String variable) {
        this(Exchange.class, forVariable(variable), INITS);
    }

    public QExchange(Path<? extends Exchange> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QExchange(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QExchange(PathMetadata metadata, PathInits inits) {
        this(Exchange.class, metadata, inits);
    }

    public QExchange(Class<? extends Exchange> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new kr.co.lotteon.entity.user.QUser(forProperty("user")) : null;
    }

}

