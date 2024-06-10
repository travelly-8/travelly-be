package com.demo.travellybe.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOperationDay is a Querydsl query type for OperationDay
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOperationDay extends EntityPathBase<OperationDay> {

    private static final long serialVersionUID = -1438667654L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOperationDay operationDay = new QOperationDay("operationDay");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<OperationHour, QOperationHour> operationHours = this.<OperationHour, QOperationHour>createList("operationHours", OperationHour.class, QOperationHour.class, PathInits.DIRECT2);

    public final QProduct product;

    public QOperationDay(String variable) {
        this(OperationDay.class, forVariable(variable), INITS);
    }

    public QOperationDay(Path<? extends OperationDay> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOperationDay(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOperationDay(PathMetadata metadata, PathInits inits) {
        this(OperationDay.class, metadata, inits);
    }

    public QOperationDay(Class<? extends OperationDay> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

