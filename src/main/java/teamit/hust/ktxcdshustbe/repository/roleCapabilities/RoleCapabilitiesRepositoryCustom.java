package teamit.hust.ktxcdshustbe.repository.roleCapabilities;

import teamit.hust.ktxcdshustbe.entity.RoleCapabilities;

import java.util.List;

public interface RoleCapabilitiesRepositoryCustom {
    List<RoleCapabilities> findRoleCapabilitiesByIdRole(Integer idRole);
}
