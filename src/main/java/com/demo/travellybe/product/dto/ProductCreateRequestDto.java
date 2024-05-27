package com.demo.travellybe.product.dto;

import com.demo.travellybe.product.domain.TicketPrice;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ProductCreateRequestDto {
    @NotNull
    private String name;

    @NotNull
    private int price;

    @NotNull
    private String type;

    @NotNull
    private String description;

    private String imageUrl;

    @NotNull
    private String address;
    private String detailAddress;

    @NotNull
    private String phoneNumber;

    private String homepage;

    @NotNull
    private String cityCode;

    @NotNull
    private int ticketCount;
    private TicketPrice ticketPrice;

    private Long memberId;

    @NotNull
    private List<OperationDayDto> operationDays;
}