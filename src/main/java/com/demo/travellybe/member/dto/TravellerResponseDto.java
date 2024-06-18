package com.demo.travellybe.member.dto;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.response.MyProductResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TravellerResponseDto {

    private String nickname;

    private String email;

    private String imageUrl;

    private int point;

    @Schema(description = "작성하지 않은 리뷰 개수")
    private int remainReviewCount;

    @Schema(description = "일정이 지나지 않은 예약 개수")
    private int notPassedReservations;

    @Schema(description = "최근 본 상품")
    private List<MyProductResponseDto> recentProducts;


    public TravellerResponseDto(Member member, int reviewCount, int reservationCount, List<MyProductResponseDto> recentProducts) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.point = member.getPoint();
        this.imageUrl = member.getImageUrl();
        this.remainReviewCount = reviewCount;
        this.notPassedReservations = reservationCount;
        this.recentProducts = recentProducts;
    }
}
