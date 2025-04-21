package kr.co.lotteon.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1383569261L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final NumberPath<Integer> hit = createNumber("hit", Integer.class);

    public final kr.co.lotteon.entity.category.QMainCategory mainCategory;

    public final StringPath prodContent = createString("prodContent");

    public final NumberPath<Integer> prodDeliveryFree = createNumber("prodDeliveryFree", Integer.class);

    public final NumberPath<Integer> prodDiscount = createNumber("prodDiscount", Integer.class);

    public final StringPath prodName = createString("prodName");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final NumberPath<Integer> prodPoint = createNumber("prodPoint", Integer.class);

    public final NumberPath<Integer> prodPrice = createNumber("prodPrice", Integer.class);

    public final NumberPath<Integer> prodSold = createNumber("prodSold", Integer.class);

    public final NumberPath<Integer> prodStock = createNumber("prodStock", Integer.class);

    public final NumberPath<Double> ratingAvg = createNumber("ratingAvg", Double.class);

    public final NumberPath<Double> ratingTotal = createNumber("ratingTotal", Double.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final kr.co.lotteon.entity.seller.QSeller seller;

    public final kr.co.lotteon.entity.category.QSubCategory subCategory;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mainCategory = inits.isInitialized("mainCategory") ? new kr.co.lotteon.entity.category.QMainCategory(forProperty("mainCategory")) : null;
        this.seller = inits.isInitialized("seller") ? new kr.co.lotteon.entity.seller.QSeller(forProperty("seller"), inits.get("seller")) : null;
        this.subCategory = inits.isInitialized("subCategory") ? new kr.co.lotteon.entity.category.QSubCategory(forProperty("subCategory"), inits.get("subCategory")) : null;
    }

}

