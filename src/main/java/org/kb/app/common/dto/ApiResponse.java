package org.kb.app.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    public static <T> ResponseEntity<T> success(T body) { //200
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public static <T> ResponseEntity<T> created(T body) { //201
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static <T> ResponseEntity<T> badRequest(T body) { //400
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    public static <T> ResponseEntity<T> unAuthorized(T body) { //401
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    public static <T> ResponseEntity<T> forbidden(T body) { //403
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    public static <T> ResponseEntity<T> expiredAuthentication(T body) { //417
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(body);
    }


    public static <T> ResponseEntity<T> notFound(T body) { //404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    public static <T> ResponseEntity<T> internalServerError(T body) { //500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
