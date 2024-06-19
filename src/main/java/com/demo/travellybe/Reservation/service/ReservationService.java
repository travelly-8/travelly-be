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


    void cancelReservation(Long id);
    void rejectReservation(Long id, String rejectReason);

    ReservationResponseDto getReservationData(String username, Long reservationId);
    /** 구매자 ID로 예약 조회 */
    List<ReservationResponseDto> getReservationsByBuyerId(Long memberId);
    /** 상품 ID로 상품 상세 + 예약 목록 조회 */
    MyReservationResponseDto getReservationsByProductId(Long productId);
    /** 판매자 ID로 상품별 대기 중인 예약 개수 조회 */
    List<PendingReservationsPerProductDto> getProductsBySellerId(Long sellerId);
}
