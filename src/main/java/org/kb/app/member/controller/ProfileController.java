package org.kb.app.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.kb.app.auth.dto.CustomUserDetails;
import org.kb.app.common.dto.ApiResponse;
import org.kb.app.member.dto.ProfileInfoDto;
import org.kb.app.member.dto.ProfileUpdateDto;
import org.kb.app.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "user 정보 수정")
public class ProfileController {

    private final MemberService memberService;

    @Tag(name = "Profile")
    @PatchMapping()
    @Operation(summary = "user의 닉네임 수정", description = "user 닉네임 최초 등록, 수정 API")
    public ResponseEntity<ProfileUpdateDto> updateProfile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @RequestBody @Valid ProfileUpdateDto profileUpdateDto) {
        return ApiResponse.success(memberService.updateProfile(userDetails, profileUpdateDto.getNickname()));
    }

    @Tag(name = "Profile")
    @GetMapping()
    @Operation(summary = "user 회원정보 조회", description = "user 회원정보 조회 API")
    public ResponseEntity<ProfileInfoDto> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.success(ProfileInfoDto.of(memberService.findMember(userDetails)));
    }
}
