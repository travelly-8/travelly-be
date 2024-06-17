package com.demo.travellybe.Reservation.service;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.domain.ReservationTicket;
import com.demo.travellybe.Reservation.dto.MyReservationResponseDto;
import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;
import com.demo.travellybe.Reservation.dto.ReservationTicketDto;
import com.demo.travellybe.Reservation.repository.ReservationRepository;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.product.domain.OperationDay;
import com.demo.travellybe.product.domain.OperationHour;
import com.demo.travellybe.product.domain.Product;
import com.demo.travellybe.product.domain.Ticket;
import com.demo.travellybe.product.repository.ProductRepository;
import com.demo.travellybe.product.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
        Product product = findProductById(productId);
        Member buyer = findMemberById(memberId);

        checkProductQuantity(reservationCreateDto, product);

        Reservation reservation = Reservation.of(product, buyer, reservationCreateDto.getPhoneNumber(), reservationCreateDto.getDate(), reservationCreateDto.getStartTime(), reservationCreateDto.getEndTime());
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

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private void checkProductQuantity(ReservationCreateDto reservationCreateDto, Product product) {
        int totalQuantity = reservationCreateDto.getTicketDtos().stream()
                .mapToInt(ReservationTicketDto::getQuantity)
                .sum();
        if (product.getQuantity() < totalQuantity)
            throw new CustomException(ErrorCode.PRODUCT_NOT_ENOUGH_TICKET_QUANTITY);
        product.updateStock(product.getQuantity() - totalQuantity);
    }

    @Override
    public ReservationResponseDto getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
        return new ReservationResponseDto(reservation);
    }

    @Override
    public void checkProductOwner(Long productId, Long memberId) {
        Product product = findProductById(productId);
        if (product.getMember().getId().equals(memberId)) throw new CustomException(ErrorCode.RESERVATION_SELF_PRODUCT);
    }

    @Override
    public void checkOperationDateTime(Long productId, ReservationCreateDto reservationCreateDto) {
        Product product = findProductById(productId);
        // 예약 시간이 없을 경우 하루 중 최대 시간으로 설정
        if (reservationCreateDto.getStartTime() == null && reservationCreateDto.getEndTime() == null) {
            reservationCreateDto.setStartTime(LocalTime.of(0, 1));
            reservationCreateDto.setEndTime(LocalTime.of(23, 59));
        }

        Optional<OperationDay> day = product.getOperationDays().stream()
                .filter(operationDay -> operationDay.getDate().equals(reservationCreateDto.getDate()))
                .findAny();
        if (day.isEmpty()) throw new CustomException(ErrorCode.PRODUCT_NOT_AVAILABLE_OPERATION_DAY);

        List<OperationHour> hours = day.get().getOperationHours();
        if (hours.stream().noneMatch(hour -> hour.getStartTime().equals(reservationCreateDto.getStartTime())
                && hour.getEndTime().equals(reservationCreateDto.getEndTime()))) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_AVAILABLE_OPERATION_DAY);
        }
    }

    @Override
    public ReservationResponseDto updateStatus(Long id, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.updateStatus(status);
        return new ReservationResponseDto(reservation);
    }

    @Override
    public void checkSeller(Long reservationId, Long memberId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
        Member seller = reservation.getProduct().getMember();
        if (!seller.getId().equals(memberId)) throw new CustomException(ErrorCode.RESERVATION_FORBIDDEN);
    }

    @Override
    public List<ReservationResponseDto> getReservationsByMemberId(Long memberId) {
        return reservationRepository.findByBuyerId(memberId).stream()
                .map(ReservationResponseDto::new)
                .toList();
    }

    public MyReservationResponseDto getReservationsByProductId(Long memberId, Long productId) {

        // 상품 검색
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 예약 검색
        List<Reservation> reservations = reservationRepository.findByBuyerId(memberId);

        return new MyReservationResponseDto(product, reservations);
    }
}
