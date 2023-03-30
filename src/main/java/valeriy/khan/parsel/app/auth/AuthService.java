package valeriy.khan.parsel.app.auth;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import valeriy.khan.parsel.app.auth.dto.LoginRequest;
import valeriy.khan.parsel.app.auth.dto.LoginResponse;
import valeriy.khan.parsel.app.auth.dto.RegisterUserRequest;
import valeriy.khan.parsel.app.auth.dto.RegisterUserResponse;
import valeriy.khan.parsel.app.messageProvider.MessageProvider;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final FeignAuthClient feignAuthClient;
    private final MessageProvider messageProvider;
    private final KeycloakBack keycloakBack;
    @Autowired
    private final KeycloakProperties properties;

    public ResponseEntity<?> login(LoginRequest request) {
        Map<String, String> requestBodyMap = keycloakBack.createRequestBodyMap(request.getUsername(), request.getPassword(), properties.getClientId(), properties.getGrantTypePassword());
        LoginResponse login;
        try {
            login = feignAuthClient.login(requestBodyMap);
            return messageProvider.successLogin(login);
        } catch (FeignException.FeignClientException ex) {
            return messageProvider.failedLogin();
        }
    }

    public ResponseEntity<?> registerUser(RegisterUserRequest request) {
        String adminAccessToken = keycloakBack.getAdminAccessToken(properties);
        String authorizationHeaderValue = "Bearer " + adminAccessToken;
        ResponseEntity<?> responseEntity = feignAuthClient.registerUser(authorizationHeaderValue, request);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return messageProvider.successRegister();
        }
        return messageProvider.failedRegister();
    }
}
