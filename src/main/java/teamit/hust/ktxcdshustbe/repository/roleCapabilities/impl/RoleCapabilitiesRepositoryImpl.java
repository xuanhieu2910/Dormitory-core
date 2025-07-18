package teamit.hust.ktxcdshustbe.repository.roleCapabilities.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.RoleCapabilities;
import teamit.hust.ktxcdshustbe.repository.roleCapabilities.RoleCapabilitiesRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class RoleCapabilitiesRepositoryImpl implements RoleCapabilitiesRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<RoleCapabilities> findRoleCapabilitiesByIdRole(Integer idRole) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_role_capabilities,  " +
                "       id_role,  " +
                "       id_capabilities,  " +
                "       permission,  " +
                "       time_created,  " +
                "       time_modified,  " +
                "       id_user_modified  " +
                "from role_capabilities  " +
                "where role_capabilities.id_role = :idRole ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRole", idRole);
        List<Object[]> result = query.getResultList();
        List<RoleCapabilities> roleCapabilitiesList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                RoleCapabilities roleCapabilities = new RoleCapabilities();
                roleCapabilities.setIdRoleCapabilities(ValueUtil.getIntegerByObject(obj[0]));
                roleCapabilities.setIdRole(ValueUtil.getIntegerByObject(obj[1]));
                roleCapabilities.setIdCapabilities(ValueUtil.getIntegerByObject(obj[2]));
                roleCapabilities.setPermission(ValueUtil.getIntegerByObject(obj[3]));
                roleCapabilities.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
                roleCapabilities.setTimeModified(ValueUtil.getLongByObject(obj[5]));
                roleCapabilities.setIdUserModified(ValueUtil.getIntegerByObject(obj[6]));
                roleCapabilitiesList.add(roleCapabilities);
            }
        }
        return roleCapabilitiesList;
    }

}
