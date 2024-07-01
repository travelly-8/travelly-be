package com.demo.travellybe.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class ReviewPageRequestDto {
    @NotNull
    @Schema(example = "0")
    private int page;
    @NotNull
    @Schema(example = "10")
    private int size;
    @Schema(description = "정렬 방식", example = "new/old/lowRating/highRating")
    private String sort = "new";

    public Pageable toPageable() {
        // new는 최신순, old는 오래된순, low-rating은 낮은 평점순, high-rating은 높은 평점순
        String sortField = switch (sort) {
            case "lowRating", "highRating" -> "rating";
            default -> "createdDate";
        };

        Sort sort;
        if (this.sort.equals("lowRating") || this.sort.equals("old")) {
            sort = Sort.by(sortField).ascending();
        } else {
            sort = Sort.by(sortField).descending();
        }

        return PageRequest.of(page, size, sort);
    }
}
