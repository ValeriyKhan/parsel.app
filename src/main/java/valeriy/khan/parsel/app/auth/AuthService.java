package valeriy.khan.parsel.app.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import valeriy.khan.parsel.app.auth.dto.*;
import valeriy.khan.parsel.app.messageprovider.MessageProvider;
import valeriy.khan.parsel.app.utils.Utils;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;

import static feign.FeignException.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final FeignAuthClient feignAuthClient;
    private final MessageProvider messageProvider;
    private final KeycloakBack keycloakBack;
    private final Utils utils;
    @Autowired
    private final KeycloakProperties properties;

    public ResponseEntity<?> login(LoginRequest request) {
        Map<String, String> requestBodyMap = keycloakBack.createRequestBodyWithLoginAndPassword(
                request.getUsername(),
                request.getPassword(),
                properties.getClientId(),
                properties.getGrantTypePassword());
        Response loginResponse;
        try {
            loginResponse = feignAuthClient.login(requestBodyMap);
            GetTokensResponse tokensResponse = keycloakBack.generateResponse(loginResponse);
            return messageProvider.responseFeignMessage(tokensResponse);
        } catch (FeignClientException | IOException ex) {
            return messageProvider.failedLogin();
        }
    }

    public ResponseEntity<?> registerUser(RegisterUserRequest request) { //Method has Aspect of catching an OpenFeignException
        if (request.getPassword().equals(request.getConfirmPassword())) {
            String adminAccessToken = keycloakBack.getAdminAccessToken(properties);
            String authorizationHeaderValue = "Bearer " + adminAccessToken;
            try {
                FeignRegisterUserRequest registerUserRequest = keycloakBack.prepareUserRequest(request);
                Response registerUserResponse = feignAuthClient.registerUser(authorizationHeaderValue, registerUserRequest);
                if (registerUserResponse.status() <= 200 || registerUserResponse.status() > 300) { //check if register is success
                    GetTokensResponse tokensResponse = keycloakBack.generateResponse(registerUserResponse);
                    return messageProvider.responseFeignMessage(tokensResponse);
                }
                return setRoleToUser(request, authorizationHeaderValue);
            } catch (Exception ex) {
                return messageProvider.failedRegisterUser();
            }
        }
        return messageProvider.passwordsDontMatch();
    }

    private ResponseEntity<?> setRoleToUser(RegisterUserRequest request, String authorizationHeaderValue) throws IOException {
        try {
            ResponseEntity<?> login = login(new LoginRequest(request.getUsername(), request.getPassword()));
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse loginResponse = mapper.convertValue(login.getBody(), LoginResponse.class);
            String userId = keycloakBack.getUserId(loginResponse);
            if (login.getStatusCode().is2xxSuccessful()) {
                FeignSetRoleRequest[] arrayBody = {properties.getSetRoleRequestBody()};
                Response setRoleResponse = feignAuthClient.setRoleToUser(authorizationHeaderValue, arrayBody, userId);
                if (setRoleResponse.status() >= 200 && setRoleResponse.status() < 300) {
                    GetTokensResponse response = keycloakBack.generateResponse(setRoleResponse);
                    return messageProvider.responseFeignMessage(response);
                }
            }
            feignAuthClient.deleteUser(authorizationHeaderValue, userId);
            return messageProvider.failedRegisterUser();
        } catch (Exception ex) {
            return messageProvider.failedRegisterUser();
        }
    }

    public ResponseEntity<?> getTokens(GetTokensRequest request) {
        Map<String, String> requestBodyMap = keycloakBack.createRequestBodyWithRefreshToken(
                request.getRefreshToken(),
                properties.getClientId(),
                properties.getGrantTypeRefreshToken());
        try {
            Response response = feignAuthClient.getTokens(requestBodyMap);
            GetTokensResponse getTokensResponse = keycloakBack.generateResponse(response);
            return messageProvider.responseFeignMessage(getTokensResponse);
        } catch (Exception ex) {
            return messageProvider.failedLogin();
        }
    }

    public ResponseEntity<?> changePassword(Principal principal, ChangePasswordRequest request) {
        if (keycloakBack.isPasswordsMatch(request.getNewPassword(), request.getConfirmNewPassword())) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
            String username = (String) token.getTokenAttributes().get("preferred_username");
            Map<String, String> requestBodyWithLoginAndPassword = keycloakBack.createRequestBodyWithLoginAndPassword(
                    username,
                    request.getOldPassword(),
                    properties.getClientId(),
                    properties.getGrantTypePassword());
            try {
                Response loginResponse = feignAuthClient.login(requestBodyWithLoginAndPassword);
                if (loginResponse.status() < 200 || loginResponse.status() > 300) {
                    GetTokensResponse tokensResponse = keycloakBack.generateResponse(loginResponse);
                    return messageProvider.responseFeignMessage(tokensResponse);
                }
                String adminAccessToken = keycloakBack.getAdminAccessToken(properties);
                String authHeader = "Bearer " + adminAccessToken;
                FeignChangePasswordRequest feignChangePasswordRequest = FeignChangePasswordRequest.builder()
                        .password(request.getNewPassword())
                        .temporary(false)
                        .type("password")
                        .build();
                String userId = (String) token.getTokenAttributes().get("sub");
                Response changePasswordResponse = feignAuthClient.changePassword(authHeader, feignChangePasswordRequest, userId);
                GetTokensResponse tokensResponse = keycloakBack.generateResponse(changePasswordResponse);
                return messageProvider.responseFeignMessage(tokensResponse);
            } catch (FeignClientException ex) {
                return messageProvider.wrongPassword();
            } catch (Exception ex) {
                return messageProvider.failedLogin();
            }
        }
        return messageProvider.passwordsDontMatch();
    }
}
