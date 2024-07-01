package com.demo.travellybe.comment.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.comment.dto.CommentDto;
import com.demo.travellybe.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/{reviewId}/{commentId}")
    @Operation(summary = "댓글 등록")
    public ResponseEntity<Void> saveComment(
            @RequestBody CommentDto commentDto,
            @PathVariable(value = "reviewId") Long reviewId,
            @PathVariable(value = "commentId") Long commentId,
            @AuthenticationPrincipal PrincipalDetails userInfo
    ){
        commentService.saveComment(commentDto.getContent(), reviewId, commentId, userInfo.getUsername());
        return ResponseEntity.ok().build();
    }
}
