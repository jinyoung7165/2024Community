package org.kb.app.auth.controller;


import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.app.auth.dto.Token;
import org.kb.app.auth.dto.request.RefreshRequest;
import org.kb.app.auth.dto.request.ResignRequest;
import org.kb.app.auth.dto.response.OAuthResponse;
import org.kb.app.auth.enums.Social;
import org.kb.app.auth.service.AuthService;
import org.kb.app.common.dto.ApiResponse;
import org.kb.app.common.dto.SimpleSuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "OAuth", description = "로그인, 로그아웃, 탈퇴, 토큰 갱신")
public class AuthController {

    private final AuthService authService;
    private final String tokenPrefix = "Bearer";

    @Tag(name = "OAuth")
    @Operation(summary = "네이버 로그인", description = "Header에 socialToken(네이버 accessToken) 첨부-> 유저 정보 받고 앱의 토큰 반환")
    @PostMapping(value = "/naver")
    public ResponseEntity<OAuthResponse> naverLogin(HttpServletRequest request) {
        final String socialToken = loginHeader(request);
        return ApiResponse.success(authService.login(Social.naver, socialToken));
    }

    @Tag(name = "OAuth")
    @Operation(summary = "accessToken 갱신", description = "Header에 Access, Refresh 첨부")
    @PostMapping("/refresh")
    public ResponseEntity<Token> refreshToken(HttpServletRequest request) {
        final RefreshRequest refreshHeader = refreshHeader(request);
        return ApiResponse.success(authService.refreshToken(refreshHeader));
    }

    @Tag(name = "OAuth")
    @Operation(summary = "로그아웃", description = "Header에 Authorization 첨부: 해당 accessToken의 접근 막음")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SimpleSuccessResponse> logout(HttpServletRequest request) {
        return ApiResponse.success(authService.logout(request));
    }

    @Tag(name = "OAuth")
    @Operation(summary = "탈퇴", description = "Header에 Authorization 첨부")
    @PostMapping("/resign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SimpleSuccessResponse> resign(HttpServletRequest request,
                                                        @RequestBody ResignRequest resignRequest) {
        return ApiResponse.success(authService.resign(request, resignRequest.getReason()));
    }

    //oauth accessToken을 전달받음
    private String loginHeader(HttpServletRequest request) {
        String socialToken = request.getHeader("socialToken");
        if (StringUtils.hasText(socialToken) &&
                socialToken.startsWith(tokenPrefix)) {
            return socialToken.substring(7);
        }
        throw new RuntimeException("Header에 socialToken Bearer prefix 첨부 필요");
    }

    //refresh시 accesstoken, refreshtoken을 전달받음
    private RefreshRequest refreshHeader(HttpServletRequest request) throws JwtException {
        String accessToken = request.getHeader("Access");
        String refreshToken = request.getHeader("Refresh");
        if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken) &&
                accessToken.startsWith(tokenPrefix) && accessToken.startsWith(tokenPrefix)) {
            return RefreshRequest.builder()
                    .accessToken(accessToken.substring(7))
                    .refreshToken(refreshToken.substring(7))
                    .build();
        }
        throw new RuntimeException("Header에 Access, Refresh 모두 Bearer prefix 첨부 필요");
    }

}
