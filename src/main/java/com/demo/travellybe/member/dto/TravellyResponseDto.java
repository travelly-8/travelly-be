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
public class TravellyResponseDto {

    private String nickname;

    private String email;

    private String imageUrl;

    private int point;

    @Schema(description = "답을 달지 않은 리뷰 개수")
    private int notResponseCount;

    @Schema(description = "새로 들어온 예약 개수")
    private int newReservationCount;

    private List<MyProductResponseDto> products;

    public TravellyResponseDto(Member member, int reviewCount, int reservationCount, List<Product> products) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.point = member.getPoint();
        this.imageUrl = member.getImageUrl();
        this.notResponseCount = reviewCount;
        this.newReservationCount = reservationCount;
        this.products = products.stream()
                .map(MyProductResponseDto::new)
                .toList();
    }
}
