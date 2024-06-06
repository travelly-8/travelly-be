package com.demo.travellybe.Reservation.service;

import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;

public interface ReservationService {
    ReservationResponseDto addReservation(Long memberId, Long productId, ReservationCreateDto reservationCreateDto);
//    void getReservation(String id);
}
