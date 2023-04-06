package valeriy.khan.parsel.app.exception.handler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import valeriy.khan.parsel.app.exception.AdminAccessTokenNotFoundException;
import valeriy.khan.parsel.app.exception.UsernameAlreadyExistsException;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {
    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<?> usernameAlreadyExistsException() {
        log.error("Username already exists");
        return new ResponseEntity<>(Map.of("errorCode", "12344"), CONFLICT);
    }

    @ExceptionHandler(value = AdminAccessTokenNotFoundException.class)
    public ResponseEntity<?> adminAccessTokenNotFoundException() {
        log.error("Error with receiving admin access token from Keycloak");
        return new ResponseEntity<>(Map.of("errorCode", "12345"), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException() {
        log.error("Field received null value");
        return new ResponseEntity<>(Map.of("error", "Field should not be null"), BAD_REQUEST);
    }
}
