package teamit.hust.ktxcdshustbe.repository.userRole;

import teamit.hust.ktxcdshustbe.dto.userRole.UserRoleDto;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.UserRole;
import teamit.hust.ktxcdshustbe.response.user.FindAllRolesUserResponse;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepositoryCustom {

    List<UserRole> findAllUserRoleByIdsAndStatusActive(List<Integer> ids);
    Optional<Role> findByTitleRole(String title);
    List<FindAllRolesUserResponse> findAllRolesUserByCodeUser(String codeUser);
    List<UserRole> findUserRoleByCodeUser(String codeUser);
    Optional<UserRole> findUserRoleByIdUserRole(Integer idUserRole);
    List<UserRole> findUserRoleByIdRole(Integer idRole);
    List<UserRoleDto> findUserRoleByNameRoleAndIdDepartment(String nameRole, Integer department);
    Optional<UserRole> findUserRoleByIdUserRoleAndIdUser(Integer idUserRole, Integer idUser);
}
