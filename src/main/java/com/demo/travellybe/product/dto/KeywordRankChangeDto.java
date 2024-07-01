package com.demo.travellybe.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeywordRankChangeDto {
    private String keyword;
    private int currentRank;
    private int previousRank;
    private int rankChange;

    public KeywordRankChangeDto(String keyword, int rank, int previousRank, int rankChange) {
        this.keyword = keyword;
        this.currentRank = rank;
        this.previousRank = previousRank;
        this.rankChange = rankChange;
    }
}