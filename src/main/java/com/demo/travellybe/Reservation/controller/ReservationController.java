package com.demo.travellybe.Reservation.controller;

import com.demo.travellybe.Reservation.dto.ReservationCreateDto;
import com.demo.travellybe.Reservation.dto.ReservationResponseDto;
import com.demo.travellybe.Reservation.service.ReservationService;
import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
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
    public ReservationResponseDto getReservation(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "예약 추가", description = "상품 ID로 예약을 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "401", description = "로그인이 필요합니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "해당 상품을 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    public ReservationResponseDto addReservation(@PathVariable Long productId,
                               @RequestBody ReservationCreateDto reservationCreateDto,
                               @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) throw new CustomException(ErrorCode.LOGIN_REQUIRED);
        return reservationService.addReservation(principalDetails.getMember().getId(), productId, reservationCreateDto);
    }
}
