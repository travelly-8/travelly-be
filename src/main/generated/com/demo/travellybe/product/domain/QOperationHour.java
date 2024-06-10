package com.demo.travellybe.product.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOperationHour is a Querydsl query type for OperationHour
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOperationHour extends EntityPathBase<OperationHour> {

    private static final long serialVersionUID = -1648891706L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOperationHour operationHour = new QOperationHour("operationHour");

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QOperationDay operationDay;

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public QOperationHour(String variable) {
        this(OperationHour.class, forVariable(variable), INITS);
    }

    public QOperationHour(Path<? extends OperationHour> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOperationHour(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOperationHour(PathMetadata metadata, PathInits inits) {
        this(OperationHour.class, metadata, inits);
    }

    public QOperationHour(Class<? extends OperationHour> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.operationDay = inits.isInitialized("operationDay") ? new QOperationDay(forProperty("operationDay"), inits.get("operationDay")) : null;
    }

}

