package teamit.hust.ktxcdshustbe.response.role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindAllRoleCapabilitiesByIdRoleResponse {

    private Integer idRoleCapabilities;
    private Integer idCapability;
    private Integer permission;
    private String nameCapability;
    private String capType;
    private String component;
}
