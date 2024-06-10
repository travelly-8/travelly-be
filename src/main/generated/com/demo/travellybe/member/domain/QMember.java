package com.demo.travellybe.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 812304854L;

    public static final QMember member = new QMember("member1");

    public final com.demo.travellybe.util.QBaseTimeEntity _super = new com.demo.travellybe.util.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final ListPath<com.demo.travellybe.product.domain.Product, com.demo.travellybe.product.domain.QProduct> products = this.<com.demo.travellybe.product.domain.Product, com.demo.travellybe.product.domain.QProduct>createList("products", com.demo.travellybe.product.domain.Product.class, com.demo.travellybe.product.domain.QProduct.class, PathInits.DIRECT2);

    public final ListPath<com.demo.travellybe.Reservation.domain.Reservation, com.demo.travellybe.Reservation.domain.QReservation> reservations = this.<com.demo.travellybe.Reservation.domain.Reservation, com.demo.travellybe.Reservation.domain.QReservation>createList("reservations", com.demo.travellybe.Reservation.domain.Reservation.class, com.demo.travellybe.Reservation.domain.QReservation.class, PathInits.DIRECT2);

    public final ListPath<com.demo.travellybe.review.domain.Review, com.demo.travellybe.review.domain.QReview> reviews = this.<com.demo.travellybe.review.domain.Review, com.demo.travellybe.review.domain.QReview>createList("reviews", com.demo.travellybe.review.domain.Review.class, com.demo.travellybe.review.domain.QReview.class, PathInits.DIRECT2);

    public final EnumPath<Role> role = createEnum("role", Role.class);

    public final StringPath type = createString("type");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

