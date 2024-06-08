package com.demo.travellybe.review.service;

import com.demo.travellybe.review.dto.ReviewRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    void saveReview(List<MultipartFile> files, ReviewRequestDto reviewRequestDto, String username, Long productId);
}
