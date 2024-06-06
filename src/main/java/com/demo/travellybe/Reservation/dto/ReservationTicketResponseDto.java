package com.demo.travellybe.Reservation.dto;

import com.demo.travellybe.Reservation.domain.ReservationTicket;
import com.demo.travellybe.product.domain.Ticket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationTicketResponseDto {

        private String name;
        private int quantity;
        private int price;
        private int totalPrice;

        public ReservationTicketResponseDto(ReservationTicket reservationTicket) {
            Ticket ticket = reservationTicket.getTicket();
            this.name = ticket.getName();
            this.quantity = reservationTicket.getQuantity();
            this.price = ticket.getPrice();
            this.totalPrice = ticket.getPrice() * reservationTicket.getQuantity();
        }
}
