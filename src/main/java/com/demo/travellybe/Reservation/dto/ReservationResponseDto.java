package com.demo.travellybe.Reservation.dto;

import com.demo.travellybe.Reservation.domain.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservationResponseDto {

    private Long id;
    private String sellerName;
    private String buyerName;
    private String productName;
    private String date;
    private String startTime;
    private String endTime;
    private List<ReservationTicketResponseDto> Tickets;
    private int totalPrice;
    private String status;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.sellerName = reservation.getProduct().getMember().getNickname();
        this.buyerName = reservation.getBuyer().getNickname();
        this.productName = reservation.getProduct().getName();
        this.date = reservation.getDate().toString();
        this.startTime = reservation.getStartTime().toString();
        this.endTime = reservation.getEndTime().toString();
        this.Tickets = reservation.getReservationTickets().stream()
                .map(ReservationTicketResponseDto::new)
                .toList();
        this.totalPrice = reservation.getReservationTickets().stream()
                .mapToInt(rt -> rt.getTicket().getPrice() * rt.getQuantity())
                .sum();
        this.status = reservation.getStatus().name();
    }
}
