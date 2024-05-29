package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private String type;
    private String description;
    private String imageUrl;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private String homepage;
    private String cityCode;
    private int quantity;
    private Map<String, Integer> ticketPrice;
    private double rating;
    private List<OperationDayDto> operationDays;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.type = product.getType();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.address = product.getAddress();
        this.detailAddress = product.getDetailAddress();
        this.phoneNumber = product.getPhoneNumber();
        this.homepage = product.getHomepage();
        this.cityCode = product.getCityCode();
        this.quantity = product.getQuantity();
        this.ticketPrice = product.getTicketPrice();
        this.rating = product.getRating();
        this.operationDays = product.getOperationDays().stream().map(OperationDayDto::new).toList();
    }
}
