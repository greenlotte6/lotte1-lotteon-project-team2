package kr.co.lotteon.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserDetails is a Querydsl query type for UserDetails
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserDetails extends EntityPathBase<UserDetails> {

    private static final long serialVersionUID = -794450633L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserDetails userDetails = new QUserDetails("userDetails");

    public final StringPath gender = createString("gender");

    public final StringPath no = createString("no");

    public final StringPath rank = createString("rank");

    public final QUser user;

    public final NumberPath<Integer> userPoint = createNumber("userPoint", Integer.class);

    public QUserDetails(String variable) {
        this(UserDetails.class, forVariable(variable), INITS);
    }

    public QUserDetails(Path<? extends UserDetails> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserDetails(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserDetails(PathMetadata metadata, PathInits inits) {
        this(UserDetails.class, metadata, inits);
    }

    public QUserDetails(Class<? extends UserDetails> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

