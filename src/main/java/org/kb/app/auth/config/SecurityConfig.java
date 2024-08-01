package org.kb.app.auth.config;


import lombok.RequiredArgsConstructor;
import org.kb.app.auth.config.filter.ExceptionHandlerFilter;
import org.kb.app.auth.config.filter.JwtAuthFilter;
import org.kb.app.auth.config.handler.CustomAccessDeniedHandler;
import org.kb.app.auth.config.handler.CustomAuthenticationEntryPoint;
import org.kb.app.auth.service.AuthService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthService authService;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final JwtAuthFilter jwtAuthFilter;

    @Override
    public void configure(WebSecurity webSecurity) { //스프링 시큐리티(httpSecurity인증,인가) 적용 전
        webSecurity.ignoring().antMatchers("/favicon.ico", "/docs/**", "/configuration/**",
                "/webjars/**","/webjars/springfox-swagger-ui/*.{js,css}","/swagger-resources/configuration/**","/swagger-ui/**","/swagger**","/v3/api-docs",
                "/error", "/**/*.svg", "/**/*.css", "/**/*.js");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .headers() // 아래에 X-Frame-Option 헤더 설정을 위해 headers() 작성
                .frameOptions().sameOrigin() // 동일 도메인에서는 iframe 접근 가능하도록 X-Frame-Options을 smaeOrigin()으로 설정
                .and().cors().and()
                .httpBasic().disable()
                    .csrf().disable() //session안 쓸 것
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 관리x
                .and()
                    .authorizeRequests() //아래부터 인증 절차 설정하겠다
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/api/v1/auth/**").permitAll()
                    .antMatchers("/api/v2/**").permitAll()
                    .antMatchers("/favicon.*").anonymous()
                    //.antMatchers("/api/v1/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();


        http
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)//인증 과정에서 오류 발생
                .accessDeniedHandler(customAccessDeniedHandler); //@preauthorized권한 확인 과정에서 예외 발생 시 전달

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(exceptionHandlerFilter, JwtAuthFilter.class);
        //SpringSecurity의 UsernamePasswordAuthFilter가 실행되기 전 JwtAuthFilter먼저 실행
    }

}
