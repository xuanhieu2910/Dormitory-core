package teamit.hust.ktxcdshustbe.repository.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.request.role.FindAllRoleRequest;
import teamit.hust.ktxcdshustbe.response.role.FindDetailsRoleCapabilitiesResponse;

import java.util.List;
import java.util.Optional;

public interface RoleRepositoryCustom {
    Page<Role> findAllRole(Pageable pageable, FindAllRoleRequest findAllRoleRequest);
    Optional<Role> findByTitleRole(String titleRole);
    Optional<Role> findByShortNameRole(String shortName);
    Optional<Role> findByIdRole(Integer idRole);
    Optional<Role> findRoleByTitleOrShortName(String title, String shortName);
    List<Role> findRestRoleWithoutCurrentRole(Role role);
    List<Role> findRolesByIds(List<Integer> ids);
    FindDetailsRoleCapabilitiesResponse findDetailsRoleCapabilitiesByIdRole(Integer idRole);
    List<Role> findAllRolesByStatus(Integer status);
}
