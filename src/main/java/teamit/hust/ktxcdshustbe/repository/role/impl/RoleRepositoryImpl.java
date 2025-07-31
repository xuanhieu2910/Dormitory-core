package teamit.hust.ktxcdshustbe.repository.role.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.Capabilities;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.repository.role.RoleRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.role.FindAllRoleRequest;
import teamit.hust.ktxcdshustbe.response.role.FindAllRoleCapabilitiesByIdRoleResponse;
import teamit.hust.ktxcdshustbe.response.role.FindDetailsRoleCapabilitiesResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;

public class RoleRepositoryImpl implements RoleRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<Role> findAllRole(Pageable pageable, FindAllRoleRequest findAllRoleRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append("select role.id_role, role.title, role.status, " +
                "        role.content, role.short_name, role.description, " +
                "        role.time_created, role.time_modified  " +
                "from role  where 1 = 1 and status in (:statusNotActive, :statusActive)  ");
        setConditionalFindAllRole(findAllRoleRequest, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllRole(findAllRoleRequest, query);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<Role> roles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                Role role = new Role();
                role.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
                role.setTitle(ValueUtil.getStringByObject(obj[1]));
                role.setStatus(ValueUtil.getIntegerByObject(obj[2]));
                role.setContent(ValueUtil.getStringByObject(obj[3]));
                role.setShortName(ValueUtil.getStringByObject(obj[4]));
                role.setDescription(ValueUtil.getStringByObject(obj[5]));
                role.setTimeCreated(ValueUtil.getLongByObject(obj[6]));
                role.setTimeModified(ValueUtil.getLongByObject(obj[7]));
                roles.add(role);
            }
        }
        return new PageImpl<>(roles, pageable, countFindAllRole(findAllRoleRequest));
    }

    private long countFindAllRole(FindAllRoleRequest findAllRoleRequest) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) count    " +
                "from role  where 1 = 1  ");
        setConditionalFindAllRole(findAllRoleRequest, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllRole(findAllRoleRequest, query);
        return ValueUtil.getLongByObject(query.getFirstResult());
    }

    private void setParameterFindAllRole(FindAllRoleRequest findAllRoleRequest, Query query) {
        query.setParameter("statusNotActive", Constants.ROLE_STATUS_ACTIVE);
        query.setParameter("statusActive", Constants.ROLE_STATUS_ACTIVE);
        if (StringUtils.isNotBlank(findAllRoleRequest.getKeyword())) {
            query.setParameter("keyword", findAllRoleRequest.getKeyword());
        }
    }

    private void setConditionalFindAllRole(FindAllRoleRequest findAllRoleRequest, StringBuilder sb) {
        if (StringUtils.isNotBlank(findAllRoleRequest.getKeyword())) {
            sb.append(" and (role.short_name REGEXP :keyword ) ");
        }
        sb.append(" ORDER BY role.id_role ");
    }

    @Override
    public Optional<Role> findByTitleRole(String titleRole) {
        StringBuilder sb = new StringBuilder();
        sb.append("select role.id_role, role.title, role.status,    " +
                "                         role.content, role.short_name, role.description,     " +
                "                         role.time_created, role.time_modified,   " +
                "                         capabilities.id_capability, capabilities.name, capabilities.cap_type,   " +
                "                         capabilities.status, capabilities.component,   " +
                "                         capabilities.time_created, capabilities.time_modified   " +
                "                 from role role   " +
                "                      inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role   " +
                "                      inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability   " +
                "                 where capabilities.status = 1   " +
                "                 and roleCapabilities.permission = 1   " +
                "                 and role.title = :titleRole  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleRole", titleRole);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            Object[] roleResponse = results.get(0);
            Role role = new Role();
            role.setIdRole(ValueUtil.getIntegerByObject(roleResponse[0]));
            role.setTitle(ValueUtil.getStringByObject(roleResponse[1]));
            role.setStatus(ValueUtil.getIntegerByObject(roleResponse[2]));
            role.setContent(ValueUtil.getStringByObject(roleResponse[3]));
            role.setShortName(ValueUtil.getStringByObject(roleResponse[4]));
            role.setDescription(ValueUtil.getStringByObject(roleResponse[5]));
            role.setTimeCreated(ValueUtil.getLongByObject(roleResponse[6]));
            role.setTimeModified(ValueUtil.getLongByObject(roleResponse[7]));
            Set<Capabilities> capabilities = new HashSet<>();
            for (Object[] obj : results) {
                Capabilities capability = new Capabilities();
                capability.setIdCapability(ValueUtil.getIntegerByObject(obj[8]));
                capability.setName(ValueUtil.getStringByObject(obj[9]));
                capability.setCapType(ValueUtil.getStringByObject(obj[10]));
                capability.setStatus(ValueUtil.getIntegerByObject(obj[11]));
                capability.setComponent(ValueUtil.getStringByObject(obj[12]));
                capability.setTimeCreated(ValueUtil.getLongByObject(obj[13]));
                capability.setTimeModified(ValueUtil.getLongByObject(obj[14]));
                capabilities.add(capability);
            }
            role.setCapabilities(capabilities);
            return Optional.of(role);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByShortNameRole(String shortNameRole) {
        StringBuilder sb = new StringBuilder();
        sb.append("select role.id_role, role.title, role.status,    " +
                "                         role.content, role.short_name, role.description,     " +
                "                         role.time_created, role.time_modified,   " +
                "                         capabilities.id_capability, capabilities.name, capabilities.cap_type,   " +
                "                         capabilities.status, capabilities.component,   " +
                "                         capabilities.time_created, capabilities.time_modified   " +
                "                 from role role   " +
                "                      inner join role_capabilities roleCapabilities on role.id_role = roleCapabilities.id_role   " +
                "                      inner join capabilities capabilities on roleCapabilities.id_capabilities = capabilities.id_capability   " +
                "                 where capabilities.status = 1   " +
                "                 and roleCapabilities.permission = 1   " +
                "                 and role.short_name = :shortName  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("shortName", shortNameRole);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            Object[] roleResponse = results.get(0);
            Role role = new Role();
            role.setIdRole(ValueUtil.getIntegerByObject(roleResponse[0]));
            role.setTitle(ValueUtil.getStringByObject(roleResponse[1]));
            role.setStatus(ValueUtil.getIntegerByObject(roleResponse[2]));
            role.setContent(ValueUtil.getStringByObject(roleResponse[3]));
            role.setShortName(ValueUtil.getStringByObject(roleResponse[4]));
            role.setDescription(ValueUtil.getStringByObject(roleResponse[5]));
            role.setTimeCreated(ValueUtil.getLongByObject(roleResponse[6]));
            role.setTimeModified(ValueUtil.getLongByObject(roleResponse[7]));
            Set<Capabilities> capabilities = new HashSet<>();
            for (Object[] obj : results) {
                Capabilities capability = new Capabilities();
                capability.setIdCapability(ValueUtil.getIntegerByObject(obj[8]));
                capability.setName(ValueUtil.getStringByObject(obj[9]));
                capability.setCapType(ValueUtil.getStringByObject(obj[10]));
                capability.setStatus(ValueUtil.getIntegerByObject(obj[11]));
                capability.setComponent(ValueUtil.getStringByObject(obj[12]));
                capability.setTimeCreated(ValueUtil.getLongByObject(obj[13]));
                capability.setTimeModified(ValueUtil.getLongByObject(obj[14]));
                capabilities.add(capability);
            }
            role.setCapabilities(capabilities);
            return Optional.of(role);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByIdRole(Integer idRole) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_role, title, status, content, " +
                "       short_name, description, time_created,  " +
                "       time_modified " +
                "from role role  " +
                "where role.id_role = :idRole ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRole", idRole);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                Role role = new Role();
                role.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
                role.setTitle(ValueUtil.getStringByObject(obj[1]));
                role.setStatus(ValueUtil.getIntegerByObject(obj[2]));
                role.setContent(ValueUtil.getStringByObject(obj[3]));
                role.setShortName(ValueUtil.getStringByObject(obj[4]));
                role.setDescription(ValueUtil.getStringByObject(obj[5]));
                role.setTimeCreated(ValueUtil.getLongByObject(obj[6]));
                role.setTimeModified(ValueUtil.getLongByObject(obj[7]));
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Role> findRoleByTitleOrShortName(String title, String shortName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select role.id_role, role.title, role.status, " +
                "       role.content, role.short_name, role.description, " +
                "       role.time_created, role.time_modified  " +
                "from role " +
                "where role.title = :title  " +
                "   or role.short_name = :shortName ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("title", title);
        query.setParameter("shortName", shortName);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                Role role = new Role();
                role.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
                role.setTitle(ValueUtil.getStringByObject(obj[1]));
                role.setStatus(ValueUtil.getIntegerByObject(obj[2]));
                role.setContent(ValueUtil.getStringByObject(obj[3]));
                role.setShortName(ValueUtil.getStringByObject(obj[4]));
                role.setDescription(ValueUtil.getStringByObject(obj[5]));
                role.setTimeCreated(ValueUtil.getLongByObject(obj[6]));
                role.setTimeModified(ValueUtil.getLongByObject(obj[7]));
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Role> findRestRoleWithoutCurrentRole(Role currentRole) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select role.id_role, role.title, role.status, " +
                "       role.content, role.short_name, role.description, " +
                "       role.time_created, role.time_modified " +
                "from role role " +
                "where role.id_role != :idRole  and status in (-1,1) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRole", currentRole.getIdRole());
        List<Object[]> result = query.getResultList();
        List<Role> roles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                Role role = new Role();
                role.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
                role.setTitle(ValueUtil.getStringByObject(obj[1]));
                role.setStatus(ValueUtil.getIntegerByObject(obj[2]));
                role.setContent(ValueUtil.getStringByObject(obj[3]));
                role.setShortName(ValueUtil.getStringByObject(obj[4]));
                role.setDescription(ValueUtil.getStringByObject(obj[5]));
                role.setTimeCreated(ValueUtil.getLongByObject(obj[6]));
                role.setTimeModified(ValueUtil.getLongByObject(obj[7]));
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    public List<Role> findRolesByIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_role, " +
                "       title, " +
                "       status, " +
                "       content, " +
                "       short_name, " +
                "       description, " +
                "       time_created, " +
                "       time_modified " +
                "from role " +
                "where role.status = :status " +
                "  and role.id_role in (:ids) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("status", Constants.ROLE_STATUS_ACTIVE);
        query.setParameter("ids", ids);
        List<Object[]> result = query.getResultList();
        List<Role> roles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                roles.add(writeDataRole(obj));
            }
        }
        return roles;
    }

    @Override
    public FindDetailsRoleCapabilitiesResponse findDetailsRoleCapabilitiesByIdRole(Integer idRole) {
        StringBuilder sb = new StringBuilder();
        sb.append("select role.id_role, role.short_name, role.description, role.status, " +
                "       roleCapabilities.id_capabilities, roleCapabilities.permission, " +
                "       capa.name nameCapabilities, capa.cap_type, capa.component, " +
                "       roleCapabilities.id_role_capabilities " +
                "from role role " +
                "    inner join role_capabilities roleCapabilities " +
                "        on role.id_role = roleCapabilities.id_role " +
                "    inner join capabilities capa on roleCapabilities.id_capabilities = capa.id_capability " +
                "where role.id_role = :idRole ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRole", idRole);
        List<Object[]> result = query.getResultList();
        FindDetailsRoleCapabilitiesResponse response = new FindDetailsRoleCapabilitiesResponse();
        if (!CollectionUtils.isEmpty(result)) {
            Object[] data = result.get(0);
            response.setIdRole(ValueUtil.getIntegerByObject(data[0]));
            response.setNameRole(ValueUtil.getStringByObject(data[1]));
            response.setDescription(ValueUtil.getStringByObject(data[2]));
            response.setStatus(ValueUtil.getIntegerByObject(data[3]));
            List<FindAllRoleCapabilitiesByIdRoleResponse> capabilities = new ArrayList<>();
            for (Object[] obj : result) {
                FindAllRoleCapabilitiesByIdRoleResponse roleCapability = new FindAllRoleCapabilitiesByIdRoleResponse();
                roleCapability.setIdCapability(ValueUtil.getIntegerByObject(obj[4]));
                roleCapability.setPermission(ValueUtil.getIntegerByObject(obj[5]));
                roleCapability.setNameCapability(ValueUtil.getStringByObject(obj[6]));
                roleCapability.setCapType(ValueUtil.getStringByObject(obj[7]));
                roleCapability.setComponent(ValueUtil.getStringByObject(obj[8]));
                roleCapability.setIdRoleCapabilities(ValueUtil.getIntegerByObject(obj[9]));
                capabilities.add(roleCapability);
            }
            response.setCapabilities(capabilities);
        }
        return response;
    }

    @Override
    public List<Role> findAllRolesByStatus(Integer status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select role.id_role, role.title, role.status,  " +
                "       role.content, role.short_name, role.description,  " +
                "       role.time_created, role.time_modified  " +
                "from role  " +
                "where role.status = :status ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("status", status);
        List<Role> roles = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                roles.add(writeDataRole(obj));
            }
        }
        return roles;
    }

    private Role writeDataRole(Object[] obj) {
        Role role = new Role();
        role.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
        role.setTitle(ValueUtil.getStringByObject(obj[1]));
        role.setStatus(ValueUtil.getIntegerByObject(obj[2]));
        role.setContent(ValueUtil.getStringByObject(obj[3]));
        role.setShortName(ValueUtil.getStringByObject(obj[4]));
        role.setDescription(ValueUtil.getStringByObject(obj[5]));
        role.setTimeCreated(ValueUtil.getLongByObject(obj[6]));
        role.setTimeModified(ValueUtil.getLongByObject(obj[7]));
        return role;
    }
}
