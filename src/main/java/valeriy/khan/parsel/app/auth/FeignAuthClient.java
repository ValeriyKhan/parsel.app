package valeriy.khan.parsel.app.auth;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import valeriy.khan.parsel.app.auth.dto.FeignChangePasswordRequest;
import valeriy.khan.parsel.app.auth.dto.FeignSetRoleRequest;
import valeriy.khan.parsel.app.auth.dto.FeignRegisterUserRequest;

import java.util.Map;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@FeignClient(value = "jplaceholder", url = "${keycloak.auth.url}")
public interface FeignAuthClient {
    @RequestMapping(method = POST, value = "${keycloak.auth.login-uri}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    Response login(Map<String, ?> requestBodyMap);

    @RequestMapping(method = POST, value = "${keycloak.auth.user.register-uri}", consumes = APPLICATION_JSON_VALUE)
    Response registerUser(@RequestHeader("Authorization") String accessToken, @RequestBody FeignRegisterUserRequest requestBody);

    @RequestMapping(method = POST, value = "${keycloak.auth.login-uri}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    Response getTokens(Map<String, ?> requestBodyMap);

    @RequestMapping(method = PUT, value = "${keycloak.auth.change-password-uri}{user-id}/reset-password", consumes = APPLICATION_JSON_VALUE)
    Response changePassword(@RequestHeader("Authorization") String authHeader, @RequestBody FeignChangePasswordRequest request, @PathVariable("user-id") String userId);

    @RequestMapping(method = POST, value = "${keycloak.auth.set-role-uri}{user-id}/role-mappings/realm", consumes = APPLICATION_JSON_VALUE)
    Response setRoleToUser(@RequestHeader("Authorization") String authHeader, @RequestBody FeignSetRoleRequest[] request, @PathVariable("user-id") String userId);

    @RequestMapping(method = DELETE, value = "${keycloak.auth.delete-user-uri}{user-id}")
    void deleteUser(@RequestHeader("Authorization") String authHeader,@PathVariable("user-id") String userId);
}
