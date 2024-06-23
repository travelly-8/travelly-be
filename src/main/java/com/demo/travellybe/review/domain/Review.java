package com.demo.travellybe.review.domain;

import com.demo.travellybe.comment.domain.Comment;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.util.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Size(min = 20)
    private String content;

    @Column(nullable = false)
    private int rating;

    private int likeCount = 0;
    private int commentCount = 0;

    private List<String> imageUrls;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @Builder
    public Review(String content, int rating, List<String> imageUrls) {
        this.content = content;
        this.rating = rating;
        this.imageUrls = imageUrls;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        this.commentCount++;
        comment.setReview(this);
    }


    public void update(List<String> filesUrls, ReviewRequestDto reviewRequestDto) {
        this.content = reviewRequestDto.getContent();
        this.rating = reviewRequestDto.getRating();
        this.imageUrls = filesUrls;
    }
}
