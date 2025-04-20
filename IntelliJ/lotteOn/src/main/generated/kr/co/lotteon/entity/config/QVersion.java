package kr.co.lotteon.entity.config;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVersion is a Querydsl query type for Version
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVersion extends EntityPathBase<Version> {

    private static final long serialVersionUID = -816338481L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVersion version1 = new QVersion("version1");

    public final StringPath content = createString("content");

    public final kr.co.lotteon.entity.user.QUser user;

    public final StringPath version = createString("version");

    public final NumberPath<Integer> vno = createNumber("vno", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> wdate = createDateTime("wdate", java.time.LocalDateTime.class);

    public QVersion(String variable) {
        this(Version.class, forVariable(variable), INITS);
    }

    public QVersion(Path<? extends Version> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVersion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVersion(PathMetadata metadata, PathInits inits) {
        this(Version.class, metadata, inits);
    }

    public QVersion(Class<? extends Version> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new kr.co.lotteon.entity.user.QUser(forProperty("user")) : null;
    }

}

