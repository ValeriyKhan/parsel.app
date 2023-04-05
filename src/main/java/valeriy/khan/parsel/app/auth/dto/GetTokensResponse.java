package valeriy.khan.parsel.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTokensResponse {
    private int status;
    private String reason;
    private Map<String, String> body;
}
