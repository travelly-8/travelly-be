package com.demo.travellybe.Reservation.repository;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByBuyerId(Long memberId);
    List<Reservation> findByProductId(Long productId);

    // product id로 예약 내역을 조회, 최신순으로 정렬
    List<Reservation> findByProductIdOrderByCreatedDateDesc(Long productId);

    // 유저가 예약한 상품 중 체험한 상품
    @Query("SELECT r.product FROM Reservation r WHERE r.id = :reservationId AND r.status = 'ACCEPTED'")
    Product findAcceptedProductByReservationId(@Param("reservationId") Long reservationId);
}
