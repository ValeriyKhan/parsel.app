package valeriy.khan.parsel.app.auth.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserRequest{
	private String firstName;
	private String lastName;
	private boolean emailVerified;
	private List<CredentialsItem> credentials;
	private String email;
	private boolean enabled;
	private String username;
}