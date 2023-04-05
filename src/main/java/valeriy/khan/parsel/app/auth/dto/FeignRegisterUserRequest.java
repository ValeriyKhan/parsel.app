package valeriy.khan.parsel.app.auth.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FeignRegisterUserRequest {
	private String firstName;
	private String lastName;
	private boolean emailVerified;
	private List<CredentialsItem> credentials;
	private boolean enabled;
	private String username;
}