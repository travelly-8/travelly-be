package com.demo.travellybe.Reservation.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = 426845834L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final com.demo.travellybe.util.QBaseTimeEntity _super = new com.demo.travellybe.util.QBaseTimeEntity(this);

    public final com.demo.travellybe.member.domain.QMember buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.demo.travellybe.product.domain.QProduct product;

    public final ListPath<ReservationTicket, QReservationTicket> reservationTickets = this.<ReservationTicket, QReservationTicket>createList("reservationTickets", ReservationTicket.class, QReservationTicket.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final EnumPath<ReservationStatus> status = createEnum("status", ReservationStatus.class);

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new com.demo.travellybe.member.domain.QMember(forProperty("buyer")) : null;
        this.product = inits.isInitialized("product") ? new com.demo.travellybe.product.domain.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

