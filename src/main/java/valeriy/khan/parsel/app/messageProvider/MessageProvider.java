package valeriy.khan.parsel.app.messageProvider;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import valeriy.khan.parsel.app.auth.dto.LoginResponse;

import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@Component
public class MessageProvider {
    public ResponseEntity<?> successLogin(LoginResponse response) {
        return new ResponseEntity<>(response, OK);
    }

    public ResponseEntity<?> failedLogin() {
        return new ResponseEntity<>(Map.of("error", "Wrong password or login"), FORBIDDEN);
    }

    public ResponseEntity<?> successRegister() {
        return new ResponseEntity<>(Map.of("message", "User registrated"), OK);
    }

    public ResponseEntity<?> failedRegister() {
        return new ResponseEntity<>(Map.of("error", "Failed user registration"), FORBIDDEN);
    }
}
