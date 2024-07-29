package org.kb.app.auth.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kb.app.auth.dto.CustomUserDetails;
import org.kb.app.auth.dto.Token;
import org.kb.app.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtTokenProvider {

    private final MemberRepository memberRepository;
    private final JwtValidation jwtValidation;
    @Value("${jwt.secret}")
    private String secretKey;

    private static final String tokenPrefix = "Bearer";
    private final long tokenPeriod = 1000L * 60L * 60L * 24L * 30L; //30일
    private final long refreshPeriod = 1000L * 60L * 60L * 24L * 90L; //30일

    @PostConstruct //의존성 주입 후 실행
    protected void init() { //secretKey를 BASE64 encoding
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Transactional
    public Token generateToken(CustomUserDetails userDetails) {
        String socialId = userDetails.getSocialId();
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();

        Token token = new Token(
                Jwts.builder()
                        .setSubject(socialId)
                        .claim("role", role)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey) //암호화 알고리즘, secretKey세팅
                        .compact(),
                Jwts.builder()
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact());

        saveRefreshToken(userDetails, token.getRefreshToken());
        return token;
    }

    @Transactional
    private void saveRefreshToken(CustomUserDetails userDetails, String refreshToken) {
        String socialId = userDetails.getName();
        memberRepository.updateRefreshToken(socialId, refreshToken);
    }

    //AccessToken 검사 정보로 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) { //filter에서 인증 성공 시 SecurityContext에 저장할 Authentication 생성
        Claims claims = jwtValidation.parseClaims(accessToken);
        if (claims.get("role") == null) { throw new JwtException("AccessToken Parse Failed"); } //access대신 refresh 넣었을 때 대비

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

//    public Boolean checkBlackList(String accessToken) {
//        //redis 뒤져서 accesstoken 존재하면 로그아웃/탈퇴한 유저임 -> 해당 accessToken으로 요청할 수 없음(false)
//        String inBlackList = (String) redisTemplate.opsForValue().get(accessToken);
//        if(ObjectUtils.isEmpty(inBlackList)) { return true; }
//        else { return false; }
//    }

    public Long getExpiration(String accessToken) { // accessToken 남은 유효시간
        Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public String getSocialId(String token) {
        String socialId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        return socialId;
    }


    //Client의 request 헤더 값으로 받은 토큰 값 리턴
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
            return bearerToken.substring(7);
        }
        return null;
    }

}