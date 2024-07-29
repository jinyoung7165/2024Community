package org.kb.app.common.utils;

import org.kb.app.common.dto.ErrorResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.kb.app.common.utils.CustomObjectMapper.objectMapper;


@Component
public class ErrorResponseUtils {

    public static void setExceptionResponse(HttpServletResponse response, ErrorResponse apiResponse) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(apiResponse));
        writer.flush();
    }
}
