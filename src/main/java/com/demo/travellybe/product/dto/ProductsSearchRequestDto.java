package com.demo.travellybe.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ProductsSearchRequestDto extends ProductsRequestDto {
    @Schema(example = "단어가 제목과 내용에 포함된 상품 검색")
    private String keyword;

    @Schema(example = "1:서울, 2:인천, 3:대전, 4:대구, 5:광주, 6:부산, 7:울산, 8:세종, " +
            "9:경기, 10:강원, 11:충북, 12:충남, 13:경북, 14:경남, 15:전북, 16:전남, " +
            "17:제주")
    private String cityCode;

    @Schema(example = "12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 38:쇼핑, 39:음식점")
    private String contentType;

    @Schema(example = "2024-05-31")
    private LocalDate startDate;

    @Schema(example = "2024-06-01")
    private LocalDate endDate;

    @Schema(example = "09:00")
    private String startTime;

    @Schema(example = "18:00")
    private String endTime;

    @Schema(example = "10000")
    private Integer minPrice;

    @Schema(example = "20000")
    private Integer maxPrice;

    @Builder(builderMethodName = "searchBuilder")
    public ProductsSearchRequestDto(String keyword, String cityCode, String contentType, LocalDate startDate, LocalDate endDate, String startTime, String endTime, Integer minPrice, Integer maxPrice, int page, int size, String sortField, String sortType) {
        super(page, size, sortField, sortType);
        this.keyword = keyword;
        this.cityCode = cityCode;
        this.contentType = contentType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
