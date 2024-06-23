package com.demo.travellybe.member.service;

import com.demo.travellybe.member.dto.*;
import com.demo.travellybe.product.dto.request.ProductRecentRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService {
    ProfileDto getProfile(String username);

    ProfileDto updateNickname(String username, String nickname);

    ProfileDto updatePassword(String username, String password, String newPassword);

    ProfileDto updateImage(String username, MultipartFile multipartFile);

    ProfileDto updateDefaultImage(String username);

    TravellerResponseDto getTravellerData(List<ProductRecentRequestDto> recentProducts, String username);

    TravellyResponseDto getTravellyData(String username);

    TravellyReviewResponseDto getTravellyReview(String username);

    TravellerReviewResponseDto getTravellerReview(String username);
}
