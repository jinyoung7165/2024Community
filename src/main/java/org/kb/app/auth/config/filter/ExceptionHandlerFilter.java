package org.kb.app.auth.config.filter;

import lombok.RequiredArgsConstructor;
import org.kb.app.common.dto.ErrorResponse;
import org.kb.app.common.utils.ErrorResponseUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (Exception e){
            String jwtExpired = "JWT Expired";
            if (e.getMessage().equals(jwtExpired)) {
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED); //417
            }
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            ErrorResponseUtils.setExceptionResponse(response, new ErrorResponse(e.getMessage()));
        }
    }
}
