package teamit.hust.ktxcdshustbe.service.roleCapabilities;

import teamit.hust.ktxcdshustbe.entity.RoleCapabilities;

import java.util.List;

public interface RoleCapabilitiesService {

    void saveAllRoleCapabilities(List<RoleCapabilities> roleCapabilitiesList);

    void deleteRoleCapabilitiesByIdRole(Integer idRole);

    List<RoleCapabilities> findAllRoleCapabilitiesByIdRole(Integer idRole);

}
