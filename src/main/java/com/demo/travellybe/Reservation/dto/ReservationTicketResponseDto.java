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
    @Schema(description = "이름", example = "제주도 입장권")
    private String name;
    @Schema(description = "설명", example = "제주도 입장권")
    private String description;
    @Schema(description = "수량", example = "2")
    private int quantity;
    @Schema(description = "가격", example = "10000")
    private int price;
    @Schema(description = "티켓 총 가격", example = "20000")
    private int totalPrice;

    public ReservationTicketResponseDto(ReservationTicket reservationTicket) {
        Ticket ticket = reservationTicket.getTicket();
        this.id = ticket.getId();
        this.name = ticket.getName();
        this.description = ticket.getDescription();
        this.quantity = reservationTicket.getQuantity();
        this.price = ticket.getPrice();
        this.totalPrice = ticket.getPrice() * reservationTicket.getQuantity();
    }
}
