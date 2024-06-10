package com.demo.travellybe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MEMBER_EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "M001", "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "해당 회원을 찾을 수 없습니다."),
    MEMBER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "M003", "비밀번호가 일치하지 않습니다."),
    MEMBER_NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST, "M004", "이미 존재하는 닉네임입니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "T001", "refresh 토큰값이 유효하지 않습니다."),

    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "A001", "로그인이 필요합니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "해당 상품을 찾을 수 없습니다."),
    PRODUCT_NOT_OWNER(HttpStatus.FORBIDDEN, "P002", "해당 상품의 소유자가 아닙니다."),
    PRODUCT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "P003", "해당 상품은 예약이 불가능합니다."),
    PRODUCT_NOT_ENOUGH_TICKET_QUANTITY(HttpStatus.BAD_REQUEST, "P005", "해당 상품의 최대 예약 가능 수량을 초과했습니다."),
    PRODUCT_NOT_AVAILABLE_OPERATION_DAY(HttpStatus.BAD_REQUEST, "P006", "해당 상품의 운영일이 유효하지 않습니다."),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", "해당 예약을 찾을 수 없습니다."),
    RESERVATION_SELF_PRODUCT(HttpStatus.BAD_REQUEST, "R002", "본인의 상품은 예약할 수 없습니다."),
    RESERVATION_FORBIDDEN(HttpStatus.FORBIDDEN, "R003", "권한이 없습니다."),

    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "해당 티켓을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
