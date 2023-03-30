package valeriy.khan.parsel.app.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("refresh_expires_in")
	private double refreshExpiresIn;
	@JsonProperty("not_before_policy")
	private double notBeforePolicy;
	private String scope;
	@JsonProperty("token_type")
	private String tokenType;
	@JsonProperty("session_state")
	private String sessionState;
	@JsonProperty("expires_in")
	private double expiresIn;
}