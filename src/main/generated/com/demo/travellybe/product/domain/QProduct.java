package com.demo.travellybe.product.domain;

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

    private static final long serialVersionUID = -217906678L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.demo.travellybe.util.QBaseTimeEntity _super = new com.demo.travellybe.util.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final StringPath cityCode = createString("cityCode");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath description = createString("description");

    public final StringPath detailAddress = createString("detailAddress");

    public final StringPath homepage = createString("homepage");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<ProductImage, QProductImage> images = this.<ProductImage, QProductImage>createList("images", ProductImage.class, QProductImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> maxPrice = createNumber("maxPrice", Integer.class);

    public final com.demo.travellybe.member.domain.QMember member;

    public final NumberPath<Integer> minPrice = createNumber("minPrice", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final ListPath<OperationDay, QOperationDay> operationDays = this.<OperationDay, QOperationDay>createList("operationDays", OperationDay.class, QOperationDay.class, PathInits.DIRECT2);

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final ListPath<com.demo.travellybe.Reservation.domain.Reservation, com.demo.travellybe.Reservation.domain.QReservation> reservations = this.<com.demo.travellybe.Reservation.domain.Reservation, com.demo.travellybe.Reservation.domain.QReservation>createList("reservations", com.demo.travellybe.Reservation.domain.Reservation.class, com.demo.travellybe.Reservation.domain.QReservation.class, PathInits.DIRECT2);

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final ListPath<com.demo.travellybe.review.domain.Review, com.demo.travellybe.review.domain.QReview> reviews = this.<com.demo.travellybe.review.domain.Review, com.demo.travellybe.review.domain.QReview>createList("reviews", com.demo.travellybe.review.domain.Review.class, com.demo.travellybe.review.domain.QReview.class, PathInits.DIRECT2);

    public final ListPath<Ticket, QTicket> tickets = this.<Ticket, QTicket>createList("tickets", Ticket.class, QTicket.class, PathInits.DIRECT2);

    public final StringPath type = createString("type");

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
        this.member = inits.isInitialized("member") ? new com.demo.travellybe.member.domain.QMember(forProperty("member")) : null;
    }

}

