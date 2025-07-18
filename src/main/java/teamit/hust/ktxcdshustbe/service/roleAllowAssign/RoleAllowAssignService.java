package teamit.hust.ktxcdshustbe.service.roleAllowAssign;


import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.RoleAllowAssign;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.FindRestRoleRequest;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.UpdateRoleAllowDataRequest;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindAllRoleAllowAssignResponse;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindRestRoleResponse;

import java.util.List;

public interface RoleAllowAssignService {
    List<FindAllRoleAllowAssignResponse> findAllRoleAllowAssign();
    void updateRoleAllowAssign(UpdateRoleAllowDataRequest request) ;
    void saveAllRoleAllowAssign(List<RoleAllowAssign> roleAllowAssigns);
    Page<FindRestRoleResponse> findRestRoleResponseAssign(FindRestRoleRequest request);
    void deleteRoleAssignByIdRole(Integer roleId);
}
