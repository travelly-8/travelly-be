package com.demo.travellybe.comment.domain;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.review.domain.Review;
import com.demo.travellybe.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private String nickname;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childrenComment = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(String content, String nickname, String imageUrl) {
        this.content = content;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
    public void setReview(Review review) {
        this.review = review;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setParentComment(Comment comment) {
        this.parentComment = comment;
    }

    public void addChildrenComment(Comment comment) {
        this.childrenComment.add(comment);
    }
}
