package com.demo.travellybe.Reservation.controller;

import com.demo.travellybe.Reservation.domain.ReservationStatus;
import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;
import com.demo.travellybe.Reservation.service.ReservationService;
import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.exception.ErrorResponse;
import com.demo.travellybe.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
@Tag(name = "Reservation", description = "예약 API")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{id}")
    @Operation(summary = "예약 조회", description = "예약 ID로 예약을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "404", description = "해당 예약을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservation(id));
    }

    @GetMapping("/my")
    @Operation(summary = "내 예약 조회", description = "내 예약을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<Page<ReservationResponseDto>> getMyReservations(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                          @Parameter(example = "0") int page,
                                                                          @Parameter(example = "10") int size) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("date"));
        return ResponseEntity.ok(reservationService.getReservationsByMemberId(principalDetails.getMember().getId(), pageable));
    }

    @PostMapping("/{productId}")
    @Operation(summary = "예약 추가", description = "상품 ID로 예약을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "400", description = "예약 시간이 유효하지 않습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "403", description = "상품 소유자는 예약할 수 없습니다",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<ReservationResponseDto> addReservation(@PathVariable Long productId,
                               @Valid @RequestBody ReservationCreateDto reservationCreateDto,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        Member member = principalDetails.getMember();
        // 상품 소유자는 예약할 수 없음
        reservationService.checkProductOwner(productId, member.getId());
        // 예약 시간이 유효한지 확인
        reservationService.checkOperationDateTime(productId, reservationCreateDto);
        return ResponseEntity.ok(reservationService.createReservation(member.getId(), productId, reservationCreateDto));
    }

    @PatchMapping ("/{id}/accept")
    @Operation(summary = "예약 수락", description = "상품 판매자가 예약을 수락합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ResponseEntity<ReservationResponseDto> acceptReservation(@PathVariable Long id,
                                                                    @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        return ResponseEntity.ok(reservationService.updateStatus(id, ReservationStatus.ACCEPTED));
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "예약 거절", description = "상품 판매자가 예약을 거절합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.")
            })
    public ResponseEntity<Void> rejectReservation(@PathVariable Long id,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        // TODO: 판매자만 예약 거절 가능
//        reservationService.rejectReservation(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "예약 취소", description = "상품 구매자가 예약을 취소합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.")
            })
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id,
                                                  @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        // TODO: 구매자만 예약 취소 가능
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }
}
