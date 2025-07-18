package teamit.hust.ktxcdshustbe.response.role;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FindDetailsRoleCapabilitiesResponse {

    private Integer idRole;
    private String nameRole;
    private String description;
    private Integer status;
    private List<FindAllRoleCapabilitiesByIdRoleResponse> capabilities;
}
