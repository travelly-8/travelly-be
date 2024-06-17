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
    @Schema(description = "수량", example = "2")
    private int quantity;
    @Schema(description = "가격", example = "10000")
    private int price;

    public ReservationTicketResponseDto(ReservationTicket reservationTicket) {
        Ticket ticket = reservationTicket.getTicket();
        this.id = ticket.getId();
        this.quantity = reservationTicket.getQuantity();
        this.price = ticket.getPrice();
    }
}
