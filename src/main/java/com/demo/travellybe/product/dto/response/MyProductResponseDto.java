package com.demo.travellybe.product.dto.response;

import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyProductResponseDto {

    private Long id;
    private String name;
    private String address;
    private String detailAddress;
    private List<ProductImageDto> images;

    private int price;
    private double rating;
    private int reviewCount;

    public MyProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.images = product.getImages().stream().map(ProductImageDto::new).toList();
        this.address = product.getAddress();
        this.detailAddress = product.getDetailAddress();
        this.rating = product.getRating();
        this.reviewCount = product.getReviewCount();
    }
}
