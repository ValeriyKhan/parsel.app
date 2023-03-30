package valeriy.khan.parsel.app.auth.dto;

import lombok.Data;

@Data
public class CredentialsItem{
	private boolean temporary;
	private String type;
	private String value;
}