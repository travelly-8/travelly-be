package com.demo.travellybe.Reservation.service;

import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.dto.MyReservationResponseDto;
import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto addReservation(Long memberId, Long productId, ReservationCreateDto reservationCreateDto);
    ReservationResponseDto getReservation(Long id);
    void checkProductOwner(Long productId, Long memberId);
    void checkOperationDateTime(Long productId, ReservationCreateDto reservationCreateDto);
    ReservationResponseDto updateStatus(Long id, ReservationStatus status);
    void checkSeller(Long reservationId, Long memberId);

    List<ReservationResponseDto> getReservationsByMemberId(Long memberId);

    MyReservationResponseDto getReservationsByProductId(Long id, Long productId);
}
