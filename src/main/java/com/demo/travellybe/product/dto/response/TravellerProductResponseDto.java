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
public class TravellerProductResponseDto {

    private Long id;
    private String name;
    private int price;
    private List<ProductImageDto> images;
    private List<OperationDayDto> operationDays;

    public TravellerProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getMaxPrice();
        this.images = product.getImages().stream().map(ProductImageDto::new).toList();
        this.operationDays = product.getOperationDays().stream().map(OperationDayDto::new).toList();
    }
}
