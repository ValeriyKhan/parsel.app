package valeriy.khan.parsel.app.auth;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import valeriy.khan.parsel.app.auth.dto.LoginResponse;
import valeriy.khan.parsel.app.auth.dto.RegisterUserRequest;
import valeriy.khan.parsel.app.auth.dto.RegisterUserResponse;

import java.util.Map;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;


@FeignClient(value = "jplaceholder", url = "${keycloak.auth.url}")
public interface FeignAuthClient {
    @RequestMapping(method = POST, value = "${keycloak.auth.login-uri}", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponse login(Map<String, ?> data);

    @RequestMapping(method = POST, value = "${keycloak.auth.user.register-uri}", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<?> registerUser(@RequestHeader("Authorization") String authHeader, @RequestBody RegisterUserRequest requestBody);

}
