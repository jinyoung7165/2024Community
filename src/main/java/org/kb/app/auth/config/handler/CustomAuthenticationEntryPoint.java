package org.kb.app.auth.config.handler;

import org.kb.app.common.dto.ErrorResponse;
import org.kb.app.common.utils.ErrorResponseUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint { //인증 실패

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException e) throws IOException {

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