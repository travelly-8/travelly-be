package com.demo.travellybe.review.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMemberId(Long memberId);

    @Query("SELECT COUNT(DISTINCT r) FROM Review r JOIN r.comments c WHERE c.member.id = :memberId")
    long countReviewsWithMyComments(@Param("memberId") Long memberId);

    long countByMemberId(Long memberId);

    Page<Review> findAllByProductId(Long productId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.product.id IN :products ORDER BY r.createdDate DESC")
    List<Review> findRecentReviewsByProductIds(@Param("products") List<Long> products);

    @Query("SELECT DISTINCT r FROM Review r JOIN r.product p JOIN r.comments c WHERE p.member.id = :memberId AND c.member.id = :memberId")
    List<Review> findReviewsCommentedByMember(@Param("memberId") Long memberId);
}
