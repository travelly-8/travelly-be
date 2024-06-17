package com.demo.travellybe.Reservation.dto;

import com.demo.travellybe.Reservation.domain.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ReservationResponseDto {
    @Schema(description = "예약 ID", example = "1")
    private Long id;
    @Schema(description = "구매자 이름", example = "홍길동")
    private String buyerName;

    private String phoneNumber;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<ReservationTicketResponseDto> Tickets;

    @Schema(description = "예약 총 가격", example = "20000")
    private int totalPrice;
    @Schema(example = "PENDING, REJECTED, ACCEPTED, CANCELED, EXPIRED")
    private String status;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.buyerName = reservation.getBuyer().getNickname();
        this.phoneNumber = reservation.getPhoneNumber();
        this.date = reservation.getDate();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.Tickets = reservation.getReservationTickets().stream()
                .map(ReservationTicketResponseDto::new)
                .toList();
        this.totalPrice = reservation.getReservationTickets().stream()
                .mapToInt(rt -> rt.getTicket().getPrice() * rt.getQuantity())
                .sum();
        this.status = reservation.getStatus().name();
    }
}
