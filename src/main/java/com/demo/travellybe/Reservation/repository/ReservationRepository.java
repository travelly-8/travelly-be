package com.demo.travellybe.Reservation.repository;

import com.demo.travellybe.Reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBuyerId(Long memberId);
    List<Reservation> findByProductId(Long productId);

    // product id로 예약 내역을 조회, 최신순으로 정렬
    List<Reservation> findByProductIdOrderByCreatedDateDescStartTimeDesc(Long productId);
}
