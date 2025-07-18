package teamit.hust.ktxcdshustbe.service.userRole;

import teamit.hust.ktxcdshustbe.dto.userRole.UserRoleDto;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.UserRole;
import teamit.hust.ktxcdshustbe.request.userRole.AddNewRoleDepartmentUserRequest;
import teamit.hust.ktxcdshustbe.request.userRole.UpdateUserRoleRequest;
import teamit.hust.ktxcdshustbe.response.user.FindAllRolesUserResponse;

import java.util.List;

public interface UserRoleService {

    List<UserRole> findAllUserRoleByIdsAndStatusActive(List<Integer> ids);
    Role findRoleByUserName(String name);
    UserRole saveUserRole(UserRole userRoleByRegisterAccount);


    List<FindAllRolesUserResponse> findAllRolesUserByCodeUser(String codeUser);

    List<UserRole> findUserRoleByCodeUser(String codeUser);
    List<UserRoleDto> findUserRoleByNameRoleAndIdDepartment(String nameRole, Integer department);

    void saveAllUserRole(List<UserRole> userRoles);

    void updateUserRole(UpdateUserRoleRequest request);

    void deleteUserRole(Integer idUserRole);
    void createNewUserRole(AddNewRoleDepartmentUserRequest request);

    void deleteUserRoleByIdRole(Integer idRole);
}
