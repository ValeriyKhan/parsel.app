package valeriy.khan.parsel.app.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import valeriy.khan.parsel.app.admin.dto.AddUserByAdminRequest;
import valeriy.khan.parsel.app.auth.AuthService;
import valeriy.khan.parsel.app.auth.FeignAuthClient;
import valeriy.khan.parsel.app.auth.KeycloakBack;
import valeriy.khan.parsel.app.auth.KeycloakProperties;
import valeriy.khan.parsel.app.auth.dto.*;
import valeriy.khan.parsel.app.messageprovider.MessageProvider;
import valeriy.khan.parsel.app.role.UserRole;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service

public class AdminService {
    private final KeycloakBack keycloakBack;
    private final KeycloakProperties properties;
    private final FeignAuthClient authClient;
    private final AuthService authService;
    private final MessageProvider messageProvider;

    public ResponseEntity<?> addUser(AddUserByAdminRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return messageProvider.passwordsDontMatch();
        }
        if (request.getRoles().contains(UserRole.ADMIN)) {
            return messageProvider.permissionDenied();
        }
        String adminAccessToken = keycloakBack.getAdminAccessToken(properties);
        String authorizationHeader = keycloakBack.getAuthorizationHeader(adminAccessToken);
        FeignRegisterUserRequest feignRegisterUserRequest = keycloakBack.prepareUserRequest(
                request.getFirstName(),
                request.getLastName(),
                request.getUsername(),
                request.getPassword()
        );
        Response response = authClient.registerUser(authorizationHeader, feignRegisterUserRequest);
        if (keycloakBack.isFailStatusCode(response)) {
            try {
                GetTokensResponse tokensResponse = keycloakBack.generateResponse(response);
                return messageProvider.responseFeignMessage(tokensResponse);
            } catch (IOException ex) {
                return messageProvider.failedRegisterUser();
            }

        }
        return setRoleToUser(request, authorizationHeader);
    }

    private ResponseEntity<?> setRoleToUser(AddUserByAdminRequest request, String authorizationHeaderValue) {
        try {
            ResponseEntity<?> login = authService.login(new LoginRequest(request.getUsername(), request.getPassword()));
            ObjectMapper mapper = new ObjectMapper();
            LoginResponse loginResponse = mapper.convertValue(login.getBody(), LoginResponse.class);
            String userId = keycloakBack.getUserId(loginResponse);
            if (login.getStatusCode().is2xxSuccessful()) {
                List<FeignSetRoleRequest> customRoles = properties.getCustomRoles(request.getRoles());
                Response setRoleResponse = authClient.setRoleToUser(authorizationHeaderValue, customRoles, userId);
                if (!keycloakBack.isFailStatusCode(setRoleResponse)) {
                    GetTokensResponse response = keycloakBack.generateResponse(setRoleResponse);
                    return messageProvider.responseFeignMessage(response);
                }
            }
            authClient.deleteUser(authorizationHeaderValue, userId);
            return messageProvider.failedRegisterUser();
        } catch (Exception ex) {
            return messageProvider.failedRegisterUser();
        }
    }
}
