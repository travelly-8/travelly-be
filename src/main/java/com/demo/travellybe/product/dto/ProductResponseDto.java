package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.TicketPrice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private String type;
    private String description;
    private String imageUrl;
    private String address;
    private String detailAddress;
    private String phoneNumber;
    private String homepage;
    private String cityCode;
    private int ticketCount;
    private TicketPrice ticketPrice;
    private double rating;
    private Long memberId;
    private List<OperationDayDto> operationDays;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.type = product.getType();
        this.description = product.getDescription();
        this.imageUrl = product.getImageUrl();
        this.address = product.getAddress();
        this.detailAddress = product.getDetailAddress();
        this.phoneNumber = product.getPhoneNumber();
        this.homepage = product.getHomepage();
        this.cityCode = product.getCityCode();
        this.ticketCount = product.getTicketCount();
        this.ticketPrice = product.getTicketPrice();
        this.rating = product.getRating();
        this.memberId = product.getMember().getId();
        this.operationDays = product.getOperationDays().stream().map(OperationDayDto::new).toList();
    }
}
