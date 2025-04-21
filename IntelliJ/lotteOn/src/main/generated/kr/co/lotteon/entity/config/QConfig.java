package kr.co.lotteon.entity.config;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConfig is a Querydsl query type for Config
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConfig extends EntityPathBase<Config> {

    private static final long serialVersionUID = 1517026155L;

    public static final QConfig config = new QConfig("config");

    public final StringPath addr1 = createString("addr1");

    public final StringPath addr2 = createString("addr2");

    public final StringPath businessNumber = createString("businessNumber");

    public final StringPath ceoName = createString("ceoName");

    public final NumberPath<Integer> cno = createNumber("cno", Integer.class);

    public final StringPath companyName = createString("companyName");

    public final StringPath copyright = createString("copyright");

    public final StringPath efinDispute = createString("efinDispute");

    public final StringPath email = createString("email");

    public final StringPath favicon = createString("favicon");

    public final StringPath footerLogo = createString("footerLogo");

    public final StringPath headerLogo = createString("headerLogo");

    public final StringPath hp = createString("hp");

    public final StringPath onlineSalesNumber = createString("onlineSalesNumber");

    public final StringPath subTitle = createString("subTitle");

    public final StringPath title = createString("title");

    public final StringPath workingHours = createString("workingHours");

    public QConfig(String variable) {
        super(Config.class, forVariable(variable));
    }

    public QConfig(Path<? extends Config> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConfig(PathMetadata metadata) {
        super(Config.class, metadata);
    }

}

