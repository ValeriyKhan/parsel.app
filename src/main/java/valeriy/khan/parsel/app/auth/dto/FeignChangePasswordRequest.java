package valeriy.khan.parsel.app.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FeignChangePasswordRequest {
    private String type;
    private boolean temporary;
    @JsonProperty("value")
    private String password;
}
