package com.demo.travellybe.Reservation.service;

import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.dto.PendingReservationsPerProductDto;
import com.demo.travellybe.Reservation.dto.MyReservationResponseDto;
import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto getReservation(Long id);
    ReservationResponseDto createReservation(Long memberId, Long productId, ReservationCreateDto reservationCreateDto);
    void checkProductOwner(Long productId, Long memberId);
    void checkOperationDateTime(Long productId, ReservationCreateDto reservationCreateDto);
    ReservationResponseDto updateStatus(Long id, ReservationStatus status);

    List<ReservationResponseDto> getReservationsByMemberId(Long memberId);

    void cancelReservation(Long id);

    void rejectReservation(Long id, String rejectReason);

    MyReservationResponseDto getReservationsByProductId(Long id, Long productId);

    List<PendingReservationsPerProductDto> getProductsByMemberId(Long sellerId);
}
