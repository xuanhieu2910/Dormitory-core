package teamit.hust.ktxcdshustbe.response.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamit.hust.ktxcdshustbe.response.capabilities.CapabilitiesResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RoleResponse {

    private String role;
    @JsonProperty("role_capabilities")
    private List<CapabilitiesResponse> roleCapabilities;
}
