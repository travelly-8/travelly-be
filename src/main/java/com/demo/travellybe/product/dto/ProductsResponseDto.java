package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductsResponseDto {
    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Schema(description = "상품 이름", example = "경복궁")
    private String name;

    @Schema(description = "12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 38:쇼핑, 39:음식점",
            example = "12")
    private String type;

    @Schema(description = "상품 이미지 URL", example = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png")
    private String imageUrl;

    @Schema(description = "주소", example = "서울특별시 종로구")
    private String address;

    @Schema(description = "1:서울, 2:인천, 3:대전, 4:대구, 5:광주, 6:부산, 7:울산, 8:세종, " +
            "9:경기, 10:강원, 11:충북, 12:충남, 13:경북, 14:경남, 15:전북, 16:전남, " +
            "17:제주", example = "1")
    private String cityCode;

    @Schema(description = "상품 수량", example = "100")
    private int quantity;

    @Schema(description = "티켓", example = "{\"성인\": 10000, \"청소년\": 8000}")
    private List<TicketDto> ticketDto;

    @Schema(description = "평점", example = "4.5")
    private double rating;

    @Schema(description = "리뷰 개수", example = "10")
    private int reviewCount;

    @Schema(description = "생성일")
    private LocalDateTime createdDate;

    @Schema(description = "수정일")
    private LocalDateTime modifiedDate;

    public ProductsResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.type = product.getType();
        this.imageUrl = product.getImageUrl();
        this.address = formatAddress(product.getAddress());
        this.cityCode = product.getCityCode();
        this.quantity = product.getQuantity();
        this.ticketDto = product.getTickets().stream().map(TicketDto::new).toList();
        this.rating = product.getRating();
        this.reviewCount = product.getReviewCount();
        this.createdDate = product.getCreatedDate();
        this.modifiedDate = product.getModifiedDate();
    }

    private String formatAddress(String address) {
        // 서울특별시 종로구 사직로 161 -> 서울특별시 종로구
        String[] split = address.split(" ");
        return split[0] + " " + split[1];
    }
}