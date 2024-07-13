package com.demo.travellybe.review.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.demo.travellybe.review.domain.QReview.review;
import static com.demo.travellybe.comment.domain.QComment.comment;
import static com.demo.travellybe.product.domain.QProduct.product;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(review)
                .where(review.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public long countReviewsWithMyComments(Long memberId) {
        Long count = queryFactory
                .select(review.countDistinct())
                .from(review)
                .join(review.comments, comment)
                .where(review.product.member.id.eq(memberId)
                        .and(comment.member.id.eq(memberId)))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public long countByMemberId(Long memberId) {
        Long count = queryFactory
                .select(review.count())
                .from(review)
                .where(review.member.id.eq(memberId))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public long countByProductId(Long productId) {
        Long count = queryFactory
                .select(review.count())
                .from(review)
                .where(review.product.id.eq(productId))
                .fetchOne();

        return count != null ? count : 0L;
    }

    @Override
    public Page<Review> findAllByProductId(Long productId, Pageable pageable) {
        List<Review> reviews = queryFactory
                .selectFrom(review)
                .where(review.product.id.eq(productId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(review.count())
                .from(review)
                .where(review.product.id.eq(productId))
                .fetchOne();

        long count = total != null ? total : 0L;

        return new PageImpl<>(reviews, pageable, count);
    }

    @Override
    public List<Review> findRecentReviewsByProductIds(List<Long> products) {
        return queryFactory
                .selectFrom(review)
                .where(review.product.id.in(products))
                .orderBy(review.createdDate.desc())
                .fetch();
    }

    @Override
    public List<Review> findReviewsCommentedByMember(Long memberId) {
        return queryFactory
                .selectDistinct(review)
                .from(review)
                .join(review.product, product)
                .join(review.comments, comment)
                .where(product.member.id.eq(memberId)
                        .and(comment.member.id.eq(memberId)))
                .fetch();
    }

}
