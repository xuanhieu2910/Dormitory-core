package teamit.hust.ktxcdshustbe.service.roleCapabilities.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.RoleCapabilities;
import teamit.hust.ktxcdshustbe.repository.roleCapabilities.RoleCapabilitiesRepository;
import teamit.hust.ktxcdshustbe.service.roleCapabilities.RoleCapabilitiesService;

import java.util.List;

@Service
public class RoleCapabilitiesServiceImpl implements RoleCapabilitiesService {

    @Autowired
    RoleCapabilitiesRepository roleCapabilitiesRepository;


    @Override
    public void saveAllRoleCapabilities(List<RoleCapabilities> roleCapabilitiesList) {
        roleCapabilitiesRepository.saveAll(roleCapabilitiesList);
    }

    @Modifying
    @Transactional
    @Override
    public void deleteRoleCapabilitiesByIdRole(Integer idRole) {
        List<RoleCapabilities> roleCapabilitiesList = roleCapabilitiesRepository.findRoleCapabilitiesByIdRole(idRole);
        if (!CollectionUtils.isEmpty(roleCapabilitiesList)){
            roleCapabilitiesRepository.deleteAll(roleCapabilitiesList);
        }
    }

    @Override
    public List<RoleCapabilities> findAllRoleCapabilitiesByIdRole(Integer idRole) {
        return roleCapabilitiesRepository.findRoleCapabilitiesByIdRole(idRole);
    }
}
