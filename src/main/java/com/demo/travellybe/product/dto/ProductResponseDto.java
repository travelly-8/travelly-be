package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class ProductResponseDto {
    @Schema(description = "상품 ID", example = "1")
    private Long id;
    @Schema(description = "상품 이름", example = "경복궁")
    private String name;
    @Schema(description = "12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 38:쇼핑, 39:음식점",
            example = "12")
    private String type;
    @Schema(description = "상품 설명", example = "경복궁은 조선시대 왕궁으로, 서울특별시 종로구 사직동에 위치하고 있다.")
    private String description;
    @Schema(description = "상품 이미지 URL", example = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png")
    private String imageUrl;
    @Schema(description = "주소", example = "서울특별시 종로구 사직로 161")
    private String address;
    @Schema(description = "상세 주소", example = "경복궁")
    private String detailAddress;
    @Schema(description = "전화번호", example = "01037003900")
    private String phoneNumber;
    @Schema(description = "홈페이지", example = "https://www.royalpalace.go.kr")
    private String homepage;
    @Schema(description = "1:서울, 2:인천, 3:대전, 4:대구, 5:광주, 6:부산, 7:울산, 8:세종, " +
            "9:경기, 10:강원, 11:충북, 12:충남, 13:경북, 14:경남, 15:전북, 16:전남, " +
            "17:제주", example = "1")
    private String cityCode;
    @Schema(description = "상품 수량", example = "100")
    private int quantity;
    @Schema(description = "티켓 가격", example = "{\"성인\": 10000, \"청소년\": 8000}")
    private Map<String, Integer> ticketPrice;
    @Schema(description = "평점", example = "4.5")
    private double rating;
    @Schema(description = "운영 요일 및 시간")
    private List<OperationDayDto> operationDays;

    @Schema(description = "리뷰 개수", example = "10")
    private int reviewCount;

    public ProductResponseDto(Product product, int reviewCount) {
        this.id = product.getId();
        this.name = product.getName();
        this.type = product.getType();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.address = product.getAddress();
        this.detailAddress = product.getDetailAddress();
        this.phoneNumber = product.getPhoneNumber();
        this.homepage = product.getHomepage();
        this.cityCode = product.getCityCode();
        this.quantity = product.getQuantity();
        this.ticketPrice = product.getTicketPrice();
        this.rating = product.getRating();
        this.operationDays = product.getOperationDays().stream().map(OperationDayDto::new).toList();

        this.reviewCount = reviewCount;
    }
}
