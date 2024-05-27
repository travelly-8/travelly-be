package com.demo.travellybe.product.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ProductsSearchRequestDto {
    private String cityCode;
    private String keyword;
    private String contentType;
    private String sortType;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer minPrice;
    private Integer maxPrice;
    private int page;
    private int size;

    @Builder
    public ProductsSearchRequestDto(String cityCode, String keyword, String contentType, String sortType, LocalDate date, LocalTime startTime, LocalTime endTime, Integer minPrice, Integer maxPrice, int page, int size) {
        this.cityCode = cityCode;
        this.keyword = keyword;
        this.contentType = contentType;
        this.sortType = sortType;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.page = page;
        this.size = size;
    }
}
