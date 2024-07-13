package com.demo.travellybe.review.repository;

import com.demo.travellybe.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {
    List<Review> findByMemberId(Long memberId);

    long countReviewsWithMyComments(Long memberId);

    long countByMemberId(Long memberId);

    long countByProductId(Long productId);

    Page<Review> findAllByProductId(Long productId, Pageable pageable);

    List<Review> findRecentReviewsByProductIds(List<Long> products);

    List<Review> findReviewsCommentedByMember(Long memberId);
}
