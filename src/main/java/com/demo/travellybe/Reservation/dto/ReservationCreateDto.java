package com.demo.travellybe.Reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
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

    private LocalTime startTime;
    private LocalTime endTime;
}
