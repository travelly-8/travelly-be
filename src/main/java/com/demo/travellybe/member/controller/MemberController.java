package com.demo.travellybe.member.controller;

import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.member.dto.PasswordDto;
import com.demo.travellybe.member.dto.ProfileDto;
import com.demo.travellybe.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "멤버 API")
public class MemberController {

    private final MemberService memberService;

//    @GetMapping("/my")
//    public ResponseEntity<MemberDto> myPage(@AuthenticationPrincipal PrincipalDetails userInfo) {
//        MemberDto memberDto = memberService.getUser(userInfo.getUsername());
//
//        return ResponseEntity.ok().body(memberDto);
//    }

    @GetMapping("/my/profile")
    @Operation(summary = "프로필 정보")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다")
    })
    public ResponseEntity<ProfileDto> myProfile(@AuthenticationPrincipal PrincipalDetails userInfo) {
        return ResponseEntity.ok().body(memberService.getProfile(userInfo.getUsername()));
    }

    @PutMapping("/my/profile")
    @Operation(summary = "닉네임 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "닉네임 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다")
    })
    public ResponseEntity<ProfileDto> updateNickname(@AuthenticationPrincipal PrincipalDetails userInfo, @RequestParam("nickname") String nickname) {
        return ResponseEntity.ok().body(memberService.updateNickname(userInfo.getUsername(), nickname));
    }

    @PutMapping(value = "/my/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "프로필 이미지 수정")
    public ResponseEntity<ProfileDto> updateImage(@AuthenticationPrincipal PrincipalDetails userInfo, @RequestPart(value = "file") MultipartFile multipartFile) {
        return ResponseEntity.ok().body(memberService.updateImage(userInfo.getUsername(), multipartFile));
    }

    @PutMapping("/my/profile/password")
    @Operation(summary = "비밀번호 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 수정 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다")
    })
    public ResponseEntity<ProfileDto> updatePassword(@AuthenticationPrincipal PrincipalDetails userInfo, @RequestBody PasswordDto passwordDto) {
        return ResponseEntity.ok().body(memberService.updatePassword(userInfo.getUsername(), passwordDto.getPassword(), passwordDto.getNewPassword()));
    }

}
