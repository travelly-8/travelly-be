package com.demo.travellybe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MEMBER_EMAIL_DUPLICATION(HttpStatus.CONFLICT, "M001", "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "해당 회원을 찾을 수 없습니다."),
    MEMBER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "M003", "비밀번호가 일치하지 않습니다."),
    MEMBER_NICKNAME_DUPLICATION(HttpStatus.CONFLICT, "M004", "이미 존재하는 닉네임입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
