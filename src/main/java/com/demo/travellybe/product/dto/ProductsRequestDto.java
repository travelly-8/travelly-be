package com.demo.travellybe.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class ProductsRequestDto {
    @NotNull
    @Schema(description = "페이지 번호", example = "0")
    private int page;
    @NotNull
    @Schema(description = "페이지 크기", example = "10")
    private int size;
    @Schema(example = "정렬 기준(modifiedDate, name, rating, minPrice) - 기본값 modifiedDate")
    private String sort;
    @Schema(example = "정렬 방식(asc, desc) - 기본값 desc")
    private String sortType;

    @Builder
    public ProductsRequestDto(int page, int size, String sort, String sortType) {
        this.page = page;
        this.size = size;
        // modifiedDate, name, rating, minPrice 중 하나가 아니면 modifiedDate로 설정
        this.sort = sort == null || !sort.equals("name") && !sort.equals("rating") && !sort.equals("minPrice") ? "modifiedDate" : sort;
        // asc, desc 중 하나가 아니면 desc로 설정
        this.sortType = sortType == null || !sortType.equals("asc") ? "desc" : sortType;
    }

    public Pageable toPageable() {
        Sort sort = Sort.by(this.sort);
        sort = this.sortType.equals("desc") ? sort.descending() : sort.ascending();
        return PageRequest.of(page, size, sort);
    }
}
