package com.demo.travellybe.Reservation.domain;


import com.demo.travellybe.product.domain.Ticket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTicket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_ticket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private int quantity;

    private int totalPrice;

    public static ReservationTicket of(Ticket ticket, int quantity) {
        ReservationTicket reservationTicket = new ReservationTicket();
        reservationTicket.ticket = ticket;
        reservationTicket.quantity = quantity;
        reservationTicket.totalPrice = ticket.getPrice() * quantity;
        return reservationTicket;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        reservation.getReservationTickets().add(this);
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        ticket.getReservationTickets().add(this);
    }
}
