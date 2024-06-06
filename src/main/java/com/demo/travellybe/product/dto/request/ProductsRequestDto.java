package com.demo.travellybe.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

    @Schema(description = "정렬 방식", example = "LowestPrice, HighestRating, MostReviews, Newest (기본값: Newest)")
    private String sort = "Newest";

    @Builder
    public ProductsRequestDto(int page, int size, String sort) {
        this.page = page;
        this.size = size;
        this.sort = sort != null ? sort : this.sort;
    }

    public Pageable toPageable() {
        String sortField = switch (sort) {
            case "LowestPrice" -> "minPrice";
            case "HighestRating" -> "rating";
            case "MostReviews" -> "reviewCount";
            default -> "createdDate";
        };

        Sort sort;
        if ("minPrice".equals(sortField)) {
            sort = Sort.by("minPrice").ascending();
        } else {
            sort = Sort.by(sortField);
            sort = sort.descending();
        }
        return PageRequest.of(page, size, sort);
    }
}
