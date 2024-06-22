package com.demo.travellybe.Reservation.dto;

import com.demo.travellybe.Reservation.domain.ReservationTicket;
import com.demo.travellybe.product.domain.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationTicketResponseDto {
    @Schema(description = "티켓 ID", example = "1")
    private Long id;
    @Schema(description = "티켓 이름")
    private String name;
    @Schema(description = "가격", example = "10000")
    private int price;
    @Schema(description = "수량", example = "2")
    private int quantity;

    public ReservationTicketResponseDto(ReservationTicket reservationTicket) {
        Ticket ticket = reservationTicket.getTicket();
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.quantity = reservationTicket.getQuantity();
        this.price = ticket.getPrice();
    }

    // 예약하지 않은 티켓을 위한 생성자
    public ReservationTicketResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.quantity = 0;
        this.price = ticket.getPrice();
    }
}
