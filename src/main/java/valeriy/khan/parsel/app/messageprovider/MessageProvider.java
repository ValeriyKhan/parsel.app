package valeriy.khan.parsel.app.messageprovider;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import valeriy.khan.parsel.app.auth.dto.GetTokensResponse;
import valeriy.khan.parsel.app.auth.dto.LoginResponse;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Component
public class MessageProvider {
    public ResponseEntity<?> successLogin(LoginResponse response) {
        return new ResponseEntity<>(response, OK);
    }

    public ResponseEntity<?> failedLogin() {
        return new ResponseEntity<>(Map.of("error", "Wrong password or login"), FORBIDDEN);
    }

    public ResponseEntity<?> successRegister() {
        return new ResponseEntity<>(Map.of("message", "User registered"), OK);
    }

    public ResponseEntity<?> failedRegisterUser() {
        return new ResponseEntity<>(Map.of("error", "0000002"), INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> responseFeignMessage(GetTokensResponse response) {
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getStatus()));
    }

    public ResponseEntity<?> passwordsDontMatch() {
        return new ResponseEntity<>(Map.of("error", "Passwords does not match"), BAD_REQUEST);
    }

    public ResponseEntity<?> wrongPassword() {
        return new ResponseEntity<>(Map.of("error", "Wrong password"), BAD_REQUEST);
    }

    public ResponseEntity<?> permissionDenied() {
        return new ResponseEntity<>(Map.of("error", "Permission denied"), NOT_ACCEPTABLE);
    }
}
