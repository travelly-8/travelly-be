package com.demo.travellybe.Reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class PendingReservationsPerProductDto {
    @Schema(description = "상품 ID", example = "1")
    private Long productId;
    @Schema(description = "상품 이름", example = "상품 이름")
    private String productName;
    @Schema(description = "가장 빠른 예약 가격", example = "20000")
    private int price;
    @Schema(description = "가장 빠른 예약의 date")
    private LocalDate date;
    @Schema(description = "가장 빠른 예약의 startTime")
    private LocalTime startTime;
    @Schema(description = "가장 빠른 예약의 endTime")
    private LocalTime endTime;
    @Schema(description = "예약 수", example = "3")
    private int reservationCount;
    @Schema(description = "예약 대기 중인 수", example = "1")
    private int pendingReservationCount;
}
