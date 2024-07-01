package com.demo.travellybe.Reservation.dto;

import com.demo.travellybe.Reservation.domain.Reservation;
import com.demo.travellybe.product.domain.Ticket;
import com.demo.travellybe.product.dto.ProductImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReservationResponseDto {
    @Schema(description = "예약 ID", example = "1")
    private Long id;
    @Schema(description = "상품 ID", example = "1")
    private Long productId;
    @Schema(description = "상품 이름", example = "상품 이름")
    private String productName;
    private List<ProductImageDto> productImages;
    @Schema(description = "예약자 이름", example = "홍길동")
    private String buyerName;
    @Schema(description = "연락처", example = "01012345678")
    private String phone;
    @Schema(description = "이메일", example = "email@email.com")
    private String email;

    private LocalDate date;
    private List<ReservationTicketResponseDto> tickets;

    @Schema(description = "예약 총 가격", example = "20000")
    private int totalPrice;
    @Schema(example = "PENDING, REJECTED, ACCEPTED, CANCELED, EXPIRED")
    private String status;
    @Schema(description = "거절 사유", example = "거절 사유")
    private String rejectReason;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.productId = reservation.getProduct().getId();
        this.productName = reservation.getProduct().getName();
        this.productImages = reservation.getProduct().getImages().stream().map(ProductImageDto::new).toList();
        this.buyerName = reservation.getBuyer().getNickname();
        this.phone = reservation.getPhone();
        this.email = reservation.getEmail();
        this.date = reservation.getDate();
        // 예약한 티켓
        List<ReservationTicketResponseDto> reservedTicket = reservation.getReservationTickets().stream()
                .map(ReservationTicketResponseDto::new)
                .toList();
        // 예약하지 않은 티켓
        List<Ticket> unreservedTicket = reservation.getProduct().getTickets().stream()
                .filter(t -> reservation.getReservationTickets().stream()
                        .noneMatch(rt -> rt.getTicket().getId().equals(t.getId())))
                .toList();
        List<ReservationTicketResponseDto> allTickets = new ArrayList<>();
        allTickets.addAll(reservedTicket);
        allTickets.addAll(unreservedTicket.stream().map(ReservationTicketResponseDto::new).collect(Collectors.toList()));
        this.tickets = allTickets;

        this.totalPrice = reservation.getReservationTickets().stream()
                .mapToInt(rt -> rt.getTicket().getPrice() * rt.getQuantity())
                .sum();
        this.status = reservation.getStatus().name();
        this.rejectReason = reservation.getRejectionReason();
    }
}
