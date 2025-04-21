package kr.co.lotteon.entity.feedback;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReturn is a Querydsl query type for Return
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReturn extends EntityPathBase<Return> {

    private static final long serialVersionUID = -1151367050L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReturn return$ = new QReturn("return$");

    public final StringPath content = createString("content");

    public final StringPath oName = createString("oName");

    public final NumberPath<Integer> rno = createNumber("rno", Integer.class);

    public final StringPath sName = createString("sName");

    public final StringPath type = createString("type");

    public final kr.co.lotteon.entity.user.QUser user;

    public QReturn(String variable) {
        this(Return.class, forVariable(variable), INITS);
    }

    public QReturn(Path<? extends Return> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReturn(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReturn(PathMetadata metadata, PathInits inits) {
        this(Return.class, metadata, inits);
    }

    public QReturn(Class<? extends Return> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new kr.co.lotteon.entity.user.QUser(forProperty("user")) : null;
    }

}

