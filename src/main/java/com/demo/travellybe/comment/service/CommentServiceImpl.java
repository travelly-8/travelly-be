package com.demo.travellybe.comment.service;

import com.demo.travellybe.comment.domain.Comment;
import com.demo.travellybe.comment.repository.CommentRepository;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.repository.MemberRepository;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public void saveComment(String content, Long reviewId, Long commentId, String email) {

        // 멤버 검색
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 리뷰 검색
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(content)
                .nickname(member.getNickname())
                .imageUrl(member.getImageUrl())
                .build();

        member.addComment(comment);

        if (commentId.equals(0L)) { // 일반 댓글
            review.addComment(comment);
        }else{ // 대댓글
            // 부모 댓글
            Comment parentComment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

            comment.setParentComment(parentComment);
            parentComment.addChildrenComment(comment);
        }

        commentRepository.save(comment);
    }
}
