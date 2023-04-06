package valeriy.khan.parsel.app.auth;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import valeriy.khan.parsel.app.auth.dto.FeignSetRoleRequest;
import valeriy.khan.parsel.app.role.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static valeriy.khan.parsel.app.role.UserRole.COURIER;
import static valeriy.khan.parsel.app.role.UserRole.CUSTOMER;

@ConfigurationProperties(prefix = "keycloak.auth")
@Getter
@Setter
@Configuration
@RequiredArgsConstructor
public class KeycloakProperties {
    private String url;
    private String clientId;
    private String grantTypePassword;
    private String grantTypeRefreshToken;
    private String loginUri;
    private String changePasswordUri;
    private String adminLogin;
    private String adminPassword;
    private String roleNameCustomer;
    private String roleIdCustomer;
    private String roleNameCourier;
    private String roleIdCourier;
    private final HashMap<UserRole, FeignSetRoleRequest> mapOfRoles = new HashMap<>();

    public FeignSetRoleRequest getRoleCustomer() {
        return new FeignSetRoleRequest(roleIdCustomer, roleNameCustomer);
    }

    public List<FeignSetRoleRequest> getCustomRoles(List<UserRole> roles) {
        List<FeignSetRoleRequest> requestBody = new ArrayList<>();
        HashMap<UserRole, FeignSetRoleRequest> roleRequestHashMap = getMapOfRoles();
        for (UserRole r : roles) {
            requestBody.add(roleRequestHashMap.get(r));
        }
        return requestBody;
    }

    private HashMap<UserRole, FeignSetRoleRequest> getMapOfRoles() {
        mapOfRoles.put(CUSTOMER, new FeignSetRoleRequest(roleIdCustomer, roleNameCustomer));
        mapOfRoles.put(COURIER, new FeignSetRoleRequest(roleIdCourier, roleNameCourier));
        return mapOfRoles;
    }
}
