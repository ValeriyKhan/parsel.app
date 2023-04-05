package valeriy.khan.parsel.app.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsItem{
	private boolean temporary;
	private String type;
	private String value;
}