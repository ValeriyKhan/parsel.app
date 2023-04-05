package valeriy.khan.parsel.app.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;

}
