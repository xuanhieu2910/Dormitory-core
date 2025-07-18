package teamit.hust.ktxcdshustbe.repository.roleAllowAssign;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.entity.RoleAllowAssign;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.FindRestRoleRequest;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindAllRoleAllowAssignResponse;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindRestRoleResponse;

import java.util.List;

public interface RoleAllowAssignRepositoryCustom {
    List<FindAllRoleAllowAssignResponse> findAllRoleAllowAssignResponse();

    List<RoleAllowAssign> findAllRoleAllowAssign();

    Page<FindRestRoleResponse> findRestRoleAssignResponse(Pageable pageable,
                                                          FindRestRoleRequest request,
                                                          Integer idRoleCurrent);

    void deleteRoleAssignByIdRole(Integer roleId);
}
