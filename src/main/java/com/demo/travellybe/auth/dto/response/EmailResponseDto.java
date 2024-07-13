package com.demo.travellybe.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class EmailResponseDto {

    private String email;
    private LocalDate createdDate;

    public EmailResponseDto(String email, LocalDateTime localDateTime) {
        this.email = email;
        this.createdDate = localDateTime.toLocalDate();
    }
}
