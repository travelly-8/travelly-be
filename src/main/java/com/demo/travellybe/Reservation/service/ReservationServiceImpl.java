package com.demo.travellybe.Reservation.service;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.Reservation.domain.ReservationTicket;
import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;
import com.demo.travellybe.Reservation.dto.ReservationTicketDto;
import com.demo.travellybe.Reservation.repository.ReservationRepository;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.Ticket;
import com.demo.travellybe.product.repository.ProductRepository;
import com.demo.travellybe.product.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final TicketRepository ticketRepository;

    @Override
    public ReservationResponseDto addReservation(Long memberId, Long productId, ReservationCreateDto reservationCreateDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Member buyer = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Reservation reservation = Reservation.of(product, buyer, reservationCreateDto.getDate(), reservationCreateDto.getStartTime(), reservationCreateDto.getEndTime());
        product.addReservation(reservation);
        buyer.addReservation(reservation);

        for (ReservationTicketDto ticketDto : reservationCreateDto.getTicketDtos()) {
            Ticket ticket = ticketRepository.findById(ticketDto.getTicketId())
                    .orElseThrow(() -> new CustomException(ErrorCode.TICKET_NOT_FOUND));
            ReservationTicket reservationTicket = ReservationTicket.of(ticket, ticketDto.getQuantity());
            reservation.addReservationTicket(reservationTicket);
            ticket.addReservationTicket(reservationTicket);
        }

        Reservation saved = reservationRepository.save(reservation);
        return new ReservationResponseDto(saved);
    }
}
