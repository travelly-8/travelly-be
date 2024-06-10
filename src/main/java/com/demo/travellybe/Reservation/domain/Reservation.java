package com.demo.travellybe.Reservation.domain;

import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member buyer;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationTicket> reservationTickets = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    public static Reservation of(Product product, Member buyer, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Reservation reservation = new Reservation();
        reservation.product = product;
        reservation.buyer = buyer;
        reservation.date = date;
        reservation.startTime = startTime;
        reservation.endTime = endTime;
        reservation.status = ReservationStatus.PENDING;
        return reservation;
    }

    public void addReservationTicket(ReservationTicket reservationTicket) {
        reservationTicket.setReservation(this);
    }

    public void setBuyer(Member buyer) {
        this.buyer = buyer;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }
}
