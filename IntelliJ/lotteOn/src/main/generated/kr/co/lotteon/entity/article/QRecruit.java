package kr.co.lotteon.entity.article;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRecruit is a Querydsl query type for Recruit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRecruit extends EntityPathBase<Recruit> {

    private static final long serialVersionUID = -1625531709L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRecruit recruit = new QRecruit("recruit");

    public final StringPath career = createString("career");

    public final StringPath content = createString("content");

    public final StringPath department = createString("department");

    public final StringPath employmentType = createString("employmentType");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath state = createString("state");

    public final StringPath title = createString("title");

    public final kr.co.lotteon.entity.user.QUser user;

    public final DateTimePath<java.time.LocalDateTime> wdate = createDateTime("wdate", java.time.LocalDateTime.class);

    public QRecruit(String variable) {
        this(Recruit.class, forVariable(variable), INITS);
    }

    public QRecruit(Path<? extends Recruit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRecruit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRecruit(PathMetadata metadata, PathInits inits) {
        this(Recruit.class, metadata, inits);
    }

    public QRecruit(Class<? extends Recruit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new kr.co.lotteon.entity.user.QUser(forProperty("user")) : null;
    }

}

