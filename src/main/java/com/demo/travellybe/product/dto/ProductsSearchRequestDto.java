package com.demo.travellybe.product.dto;

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
}
