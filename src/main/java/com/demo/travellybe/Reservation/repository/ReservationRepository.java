package com.demo.travellybe.Reservation.repository;

import com.demo.travellybe.Reservation.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByBuyerId(Long memberId, Pageable pageable);
}
