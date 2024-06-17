package com.demo.travellybe.Reservation.dto;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.dto.OperationDayDto;
import com.demo.travellybe.product.dto.ProductImageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyReservationResponseDto {

    // 상품 정보
    private Long productId;
    private String productName;
    private List<ProductImageDto> productImages;
    private int productPrice;
    private List<OperationDayDto> operationDays;

    // 예약 정보
    List<ReservationResponseDto> reservations;

    public MyReservationResponseDto(Product product, List<Reservation> reservations) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.productImages = product.getImages().stream().map(ProductImageDto::new).toList();
        this.productPrice = product.getMaxPrice();
        this.operationDays = product.getOperationDays().stream().map(OperationDayDto::new).toList();
        this.reservations = reservations.stream().map(ReservationResponseDto::new).toList();
    }
}
