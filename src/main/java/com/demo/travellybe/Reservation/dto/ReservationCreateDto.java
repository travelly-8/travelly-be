package com.demo.travellybe.Reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ReservationCreateDto {
    @NotNull
    private String name;

    @NotNull
    private String phone;

    @NotNull
    private String email;

    @NotNull
    private List<ReservationTicketDto> ticketDtos;

    @NotNull
    private LocalDate date;
}
