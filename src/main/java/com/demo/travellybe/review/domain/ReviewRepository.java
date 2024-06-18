package com.demo.travellybe.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMemberId(Long memberId);

    @Query("SELECT COUNT(DISTINCT r) FROM Review r JOIN r.comments c WHERE c.member.id = :memberId")
    long countReviewsWithMyComments(@Param("memberId") Long memberId);
}
