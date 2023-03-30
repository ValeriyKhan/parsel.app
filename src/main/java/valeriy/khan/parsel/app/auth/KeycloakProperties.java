package valeriy.khan.parsel.app.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
    private String registerUserLogin;
    private String registerUserPassword;
}
