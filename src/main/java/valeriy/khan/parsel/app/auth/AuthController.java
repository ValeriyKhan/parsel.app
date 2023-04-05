package valeriy.khan.parsel.app.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import valeriy.khan.parsel.app.auth.dto.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterUserRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("tokens")
    public ResponseEntity<?> getTokens(@RequestBody GetTokensRequest request) {
        return authService.getTokens(request);
    }

    @PostMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal principal) {
        return authService.changePassword(principal, request);
    }
}
