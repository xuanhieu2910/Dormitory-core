package teamit.hust.ktxcdshustbe.service.role;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.request.role.CreateNewRoleRequest;
import teamit.hust.ktxcdshustbe.request.role.FindAllRoleRequest;
import teamit.hust.ktxcdshustbe.request.role.UpdateRoleRequest;
import teamit.hust.ktxcdshustbe.response.role.FindAllRoleResponse;
import teamit.hust.ktxcdshustbe.response.role.FindDetailsRoleCapabilitiesResponse;

import java.util.List;

public interface RoleService {

    Page<FindAllRoleResponse> findAllRole(FindAllRoleRequest findAllRoleRequest);
    void createNewRole (CreateNewRoleRequest request);
    List<Role> findRoleByIds(List<Integer> ids);
    void deleteRole(Integer idRole);
    FindDetailsRoleCapabilitiesResponse findDetailsRoleCapabilitiesByIdRole(Integer idRole);
    void updateRole(UpdateRoleRequest request);
    List<Role> getAllRoleByStatus(Integer status);
    Role findRoleByTitleRole(String name);
}
