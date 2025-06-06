package kr.co.lotteon.entity.config;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTerms is a Querydsl query type for Terms
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTerms extends EntityPathBase<Terms> {

    private static final long serialVersionUID = -212752322L;

    public static final QTerms terms1 = new QTerms("terms1");

    public final StringPath finance = createString("finance");

    public final StringPath location = createString("location");

    public final NumberPath<Integer> no = createNumber("no", Integer.class);

    public final StringPath privacy = createString("privacy");

    public final StringPath tax = createString("tax");

    public final StringPath terms = createString("terms");

    public QTerms(String variable) {
        super(Terms.class, forVariable(variable));
    }

    public QTerms(Path<? extends Terms> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTerms(PathMetadata metadata) {
        super(Terms.class, metadata);
    }

}

