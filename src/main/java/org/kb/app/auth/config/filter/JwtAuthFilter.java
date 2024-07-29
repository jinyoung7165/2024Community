package org.kb.app.auth.config.filter;

import lombok.RequiredArgsConstructor;
import org.kb.app.auth.config.jwt.JwtTokenProvider;
import org.kb.app.auth.config.jwt.JwtValidation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {//jwtTokenProvider를 통해 httpRequest에서 토큰 추출, 유효성 검사 후 SecurityContext에 Auth 추가

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtValidation jwtValidation;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain) throws IOException, ServletException {

        final String token = jwtTokenProvider.resolveToken(request); //request Header 통해 accessToken받음
        if (token != null) {
            if (jwtValidation.validateToken(token)  //jwt 유효성 검사 통과
                    //&& jwtTokenProvider.checkBlackList(token)) { //블랙리스트에 없는 토큰이면
            ){//jwt인증 성공 시 SecurityContext에 해당 userDetails, 권한 정보 저장
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }

}