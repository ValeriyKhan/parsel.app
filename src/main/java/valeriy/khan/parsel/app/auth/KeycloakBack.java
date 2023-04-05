package valeriy.khan.parsel.app.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import valeriy.khan.parsel.app.auth.dto.*;
import valeriy.khan.parsel.app.exception.AdminAccessTokenNotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@AllArgsConstructor
public class KeycloakBack {
    private final FeignAuthClient authClient;
    private Map<String, String> requestBodyMap;

    public String getAdminAccessToken(KeycloakProperties properties) {
        Map<String, String> requestBodyMap = createRequestBodyWithLoginAndPassword(
                properties.getAdminLogin(),
                properties.getAdminPassword(),
                properties.getClientId(),
                properties.getGrantTypePassword());
        try {
            Response response = authClient.login(requestBodyMap);
            GetTokensResponse tokensResponse = generateResponse(response);
            return tokensResponse.getBody().get("access_token");
        } catch (IOException ex) {
            throw new AdminAccessTokenNotFoundException();
        }
    }

    public Map<String, String> createRequestBodyWithLoginAndPassword(String username, String password, String clientId, String grantType) {
        requestBodyMap = new HashMap<>();
        requestBodyMap.put("client_id", clientId);
        requestBodyMap.put("grant_type", grantType);
        requestBodyMap.put("username", username);
        requestBodyMap.put("password", password);
        return requestBodyMap;
    }

    public Map<String, String> createRequestBodyWithRefreshToken(String refreshToken, String clientId, String grantType) {
        requestBodyMap = new HashMap<>();
        requestBodyMap.put("client_id", clientId);
        requestBodyMap.put("grant_type", grantType);
        requestBodyMap.put("refresh_token", refreshToken);
        return requestBodyMap;
    }

    public Map<String, String> createRequestBodyForChangePassword(String refreshToken, String clientId, String grantType) {
        requestBodyMap = new HashMap<>();
        requestBodyMap.put("client_id", clientId);
        requestBodyMap.put("grant_type", grantType);
        requestBodyMap.put("refresh_token", refreshToken);
        return requestBodyMap;
    }

    public GetTokensResponse generateResponse(Response response) throws IOException {
        GetTokensResponse tokensResponse = new GetTokensResponse();
        String responseBody;
        if (Objects.isNull(response.body())) {
            tokensResponse.setBody(Map.of());
        } else {
            responseBody = StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8);
            if (responseBody.isEmpty()) {
                tokensResponse.setBody(Map.of());
            } else {
                HashMap<String, String> responseBodyMap = extractBodyFromResponse(responseBody);
                tokensResponse.setBody(responseBodyMap);
            }
        }
        tokensResponse.setStatus(response.status());
        tokensResponse.setReason(response.reason());
        return tokensResponse;
    }

    private HashMap<String, String> extractBodyFromResponse(String responseBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> responseBodyMap = mapper.readValue(responseBody, HashMap.class);
        return responseBodyMap;
    }


    public boolean isPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public String getUserId(LoginResponse loginResponse) throws IOException {
        String accessToken = loginResponse.getAccessToken();
        String[] chunks = accessToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> hashMap = mapper.readValue(decoder.decode(chunks[1]), HashMap.class);
        return hashMap.get("sub");
    }
    public FeignRegisterUserRequest prepareUserRequest(RegisterUserRequest request) {
        return FeignRegisterUserRequest.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailVerified(false)
                .credentials(List.of(new CredentialsItem(false, "password", request.getPassword())))
                .enabled(true)
                .username(request.getUsername())
                .build();
    }
}
