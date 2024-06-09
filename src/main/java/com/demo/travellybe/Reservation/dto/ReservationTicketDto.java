package com.demo.travellybe.Reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationTicketDto {
        @Schema(example = "1")
        private Long ticketId;
        @Schema(example = "2")
        private int quantity;
}
