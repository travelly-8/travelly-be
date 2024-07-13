package com.demo.travellybe.auth.dto.request;

import com.demo.travellybe.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignupRequestDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max=20)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@$!%*?&])([a-zA-Z0-9@$!%*?&]+)$")
    private String password;

    @NotNull
    private String nickname;

    @Schema(hidden = true)
    private String type;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .type(type)
                .build();
    }
}
