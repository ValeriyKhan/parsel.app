package valeriy.khan.parsel.app.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeignSetRoleRequest {
    @JsonProperty("id")
    private String roleId;
    @JsonProperty("name")
    private String roleName;
}
