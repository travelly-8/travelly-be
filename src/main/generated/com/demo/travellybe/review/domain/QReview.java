package com.demo.travellybe.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1641054510L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.demo.travellybe.util.QBaseTimeEntity _super = new com.demo.travellybe.util.QBaseTimeEntity(this);

    public final ListPath<com.demo.travellybe.comment.domain.Comment, com.demo.travellybe.comment.domain.QComment> comments = this.<com.demo.travellybe.comment.domain.Comment, com.demo.travellybe.comment.domain.QComment>createList("comments", com.demo.travellybe.comment.domain.Comment.class, com.demo.travellybe.comment.domain.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<String, StringPath> imageUrls = this.<String, StringPath>createList("imageUrls", String.class, StringPath.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final com.demo.travellybe.member.domain.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final com.demo.travellybe.product.domain.QProduct product;

    public final NumberPath<Integer> rating = createNumber("rating", Integer.class);

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.demo.travellybe.member.domain.QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new com.demo.travellybe.product.domain.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

