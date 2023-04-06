package valeriy.khan.parsel.app.admin.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import valeriy.khan.parsel.app.role.UserRole;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddUserByAdminRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Enumerated(EnumType.STRING)
    private List<UserRole> roles;

}
