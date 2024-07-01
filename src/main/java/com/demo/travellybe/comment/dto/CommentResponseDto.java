package com.demo.travellybe.comment.dto;

import com.demo.travellybe.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long commentId;
    private String commentUserImage;
    private String commentUserNickname;
    private LocalDate commentDate;
    private String commentContent;
    private List<CommentResponseDto> childrenComments;

    @Builder
    public CommentResponseDto(Long commentId, String commentUserImage, String commentUserNickname, LocalDate commentDate, String commentContent, List<CommentResponseDto> childrenComments) {
        this.commentId = commentId;
        this.commentUserImage = commentUserImage;
        this.commentUserNickname = commentUserNickname;
        this.commentDate = commentDate;
        this.commentContent = commentContent;
        this.childrenComments = childrenComments;
    }

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .commentUserImage(comment.getImageUrl())
                .commentUserNickname(comment.getNickname())
                .commentDate(comment.getCreatedDate().toLocalDate())
                .commentContent(comment.getContent())
                .childrenComments(comment.getChildrenComment().stream()
                        .map(CommentResponseDto::fromEntity)
                        .toList())
                .build();
    }
}
