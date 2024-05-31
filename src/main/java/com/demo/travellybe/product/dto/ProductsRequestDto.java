package com.demo.travellybe.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class ProductsRequestDto {
    @NotNull
    @Schema(description = "페이지 번호", example = "0")
    private int page;
    @NotNull
    @Schema(description = "페이지 크기", example = "10")
    private int size;
    @Schema(example = "정렬 기준(modifiedDate, name, rating, price) - 기본값 modifiedDate")
    private String sortField;
    @Schema(example = "정렬 방식(asc, desc) - 기본값 desc")
    private String sortType;

    @Builder
    public ProductsRequestDto(int page, int size, String sortField, String sortType) {
        this.page = page;
        this.size = size;
        // modifiedDate, name, rating, price 중 하나가 아니면 modifiedDate로 설정
        this.sortField = sortField == null || !sortField.equals("name") && !sortField.equals("rating") && !sortField.equals("price") ? "modifiedDate" : sortField;
        // asc, desc 중 하나가 아니면 desc로 설정
        this.sortType = sortType == null || !sortType.equals("asc") ? "desc" : sortType;
    }

    public Pageable toPageable() {
        Sort sort;
        if ("price".equals(this.sortField)) {
            sort = "asc".equals(this.sortType) ? Sort.by("minPrice").ascending() : Sort.by("maxPrice").descending();
        } else {
            sort = Sort.by(this.sortField);
            sort = "desc".equals(this.sortType) ? sort.descending() : sort.ascending();
        }
        return PageRequest.of(page, size, sort);
    }
}
