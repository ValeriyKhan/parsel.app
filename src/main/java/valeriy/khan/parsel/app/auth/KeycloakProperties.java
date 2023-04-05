package valeriy.khan.parsel.app.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import valeriy.khan.parsel.app.auth.dto.FeignSetRoleRequest;

@ConfigurationProperties(prefix = "keycloak.auth")
@Getter
@Setter
@Configuration
public class KeycloakProperties {
    private String url;
    private String clientId;
    private String grantTypePassword;
    private String grantTypeRefreshToken;
    private String loginUri;
    private String changePasswordUri;
    private String adminLogin;
    private String adminPassword;
    private String roleName;
    private String roleId;

    public FeignSetRoleRequest getSetRoleRequestBody() {
        return new FeignSetRoleRequest(roleId, roleName);
    }
}
