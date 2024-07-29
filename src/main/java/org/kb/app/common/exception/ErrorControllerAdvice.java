package org.kb.app.common.exception;

import io.jsonwebtoken.JwtException;
import org.kb.app.common.dto.ApiResponse;
import org.kb.app.common.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NoSuchElementException e) {
        return ApiResponse.notFound(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class) //RequestBody에 대한 Valid 처리
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {
        String msg = createErrMsg(e);
        return ApiResponse.badRequest(new ErrorResponse(msg));
    }

    @ExceptionHandler(value = BindException.class) //ModelAttribute에 대한 Valid 처리
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        String msg = createErrMsg(e);
        return ApiResponse.badRequest(new ErrorResponse(msg));
    }

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e) {
        String jwtExpired = "JWT Expired";
        if (e.getMessage().equals(jwtExpired)) {
            return ApiResponse.expiredAuthentication(new ErrorResponse(e.getMessage())); //417
        }
        return ApiResponse.unAuthorized(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException e) {
        return ApiResponse.badRequest(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(Exception e) {
        return ApiResponse.internalServerError(new ErrorResponse(e.getMessage()));
    }

    private String createBindErrMsg(BindingResult result) {
        StringBuilder msg = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            msg.append("[")
                    .append(error.getField())
                    .append("]")
                    .append(": ")
                    .append(error.getDefaultMessage()).
                    append("; ");
        }
        return msg.toString();
    }

    private String createErrMsg(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        return createBindErrMsg(result);
    }

    private String createErrMsg(BindException e) {
        BindingResult result = e.getBindingResult();
        return createBindErrMsg(result);
    }
}
