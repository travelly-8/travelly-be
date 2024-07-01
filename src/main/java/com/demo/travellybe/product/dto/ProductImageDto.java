package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.ProductImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageDto {
    @NotNull
    @Schema(description = "이미지 URL", example = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png")
    private String url;

    @NotNull
    @Schema(description = "이미지 순서", example = "0")
    private int order;

    @Builder
    public ProductImageDto(String url, int order) {
        this.url = url;
        this.order = order;
    }

    public ProductImageDto(ProductImage productImage) {
        this.url = productImage.getUrl();
        this.order = productImage.getImageOrder();
    }
}
