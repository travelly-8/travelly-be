package com.demo.travellybe.review.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.ErrorResponse;
import com.demo.travellybe.product.dto.response.ProductResponseDto;
import com.demo.travellybe.review.dto.ReviewRequestDto;
import com.demo.travellybe.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "Review", description = "¸®ºä API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/{productId}")
    @Operation(summary = "¸®ºä µî·Ï")
    public ResponseEntity<Void> saveReview(
            @RequestPart(value="images", required = false) List<MultipartFile> files,
            @RequestPart(value="review") ReviewRequestDto reviewRequestDto,
            @PathVariable Long productId,
            @AuthenticationPrincipal PrincipalDetails userInfo
            ) {

        reviewService.saveReview(files, reviewRequestDto, userInfo.getUsername(), productId);

        return ResponseEntity.ok().build();
    }

}
