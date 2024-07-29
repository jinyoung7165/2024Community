package org.kb.app.auth.config.handler;

import org.kb.app.common.dto.ErrorResponse;
import org.kb.app.common.utils.ErrorResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler { //권한 없는 리소스에 접근 시

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResponseUtils.setExceptionResponse(response, new ErrorResponse(e.getMessage()));
    }
}
