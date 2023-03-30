package valeriy.khan.parsel.app.auth;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import valeriy.khan.parsel.app.auth.dto.LoginResponse;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class KeycloakBack {
    private final FeignAuthClient authClient;
    public String getAdminAccessToken(KeycloakProperties properties) {
        Map<String, String> requestBodyMap = createRequestBodyMap(
                properties.getRegisterUserLogin(),
                properties.getRegisterUserPassword(),
                properties.getClientId(),
                properties.getGrantTypePassword());
        LoginResponse response = authClient.login(requestBodyMap);
        return response.getAccessToken();
    }
    public Map<String, String> createRequestBodyMap(String username, String password, String clientId, String grantType) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("client_id", clientId);
        requestBodyMap.put("grant_type", grantType);
        requestBodyMap.put("username", username);
        requestBodyMap.put("password", password);
        return requestBodyMap;
    }
}
