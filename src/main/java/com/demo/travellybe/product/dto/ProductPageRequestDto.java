package com.demo.travellybe.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class ProductPageRequestDto {
    @NotNull
    private int page;
    @NotNull
    private int size;
    private String sort;
    private String sortType;

    @Builder
    public ProductPageRequestDto(int page, int size, String sort, String sortType) {
        this.page = page;
        this.size = size;
        this.sort = sort == null ? "modifiedDate" : sort;
        this.sortType = sortType == null ? "desc" : sortType;
    }

    public Pageable toPageable() {
        Sort sort = Sort.by(this.sort);
        sort = this.sortType.equals("desc") ? sort.descending() : sort.ascending();
        return PageRequest.of(page, size, sort);
    }
}
