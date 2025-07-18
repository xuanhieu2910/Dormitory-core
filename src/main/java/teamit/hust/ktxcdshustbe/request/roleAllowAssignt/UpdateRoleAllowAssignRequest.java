package teamit.hust.ktxcdshustbe.request.roleAllowAssignt;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRoleAllowAssignRequest {


    private Integer idSourceRoleAssign;
    private String nameSourceRoleAssign;
    private List<ListDestinationRoleAssignRequest> destinationRoleAssign;
}
