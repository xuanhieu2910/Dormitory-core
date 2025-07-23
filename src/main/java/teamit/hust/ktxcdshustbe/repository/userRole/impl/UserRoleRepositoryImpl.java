package teamit.hust.ktxcdshustbe.repository.userRole.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.userRole.DepartmentUserRoleDto;
import teamit.hust.ktxcdshustbe.dto.userRole.UserRoleDto;
import teamit.hust.ktxcdshustbe.entity.Capabilities;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.UserRole;
import teamit.hust.ktxcdshustbe.repository.userRole.UserRoleRepositoryCustom;
import teamit.hust.ktxcdshustbe.response.user.FindAllRolesUserResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;

public class UserRoleRepositoryImpl implements UserRoleRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<UserRole> findAllUserRoleByIdsAndStatusActive(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ur.id_user_role, ur.id_user, ur.id_role, " +
                "       ur.time_created, ur.time_modified, ur.picked " +
                "from user_role ur  " +
                "       inner join qldt_user qu on ur.id_user = qu.id_user  " +
                "       inner join role role on ur.id_role = role.id_role  " +
                "where ur.id_user_role in (:ids) " +
                "and role.status = :statusRole and qu.is_actived = :userActive ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("ids", ids);
        query.setParameter("statusRole", Constants.ROLE_STATUS_ACTIVE);
        query.setParameter("userActive", Constants.ACCOUNT_IS_ACTIVED);
        List<Object[]> result = query.getResultList();
        List<UserRole> userRoles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result) {
                UserRole userRole = new UserRole();
                userRole.setIdUserRole(ValueUtil.getIntegerByObject(obj[0]));
                userRole.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                userRole.setIdRole(ValueUtil.getIntegerByObject(obj[2]));
                userRole.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                userRole.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                userRole.setPicked(ValueUtil.getIntegerByObject(obj[5]));
                userRoles.add(userRole);
            }
        }
        return userRoles;
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
            for(Object[] obj: results){
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
    public List<FindAllRolesUserResponse> findAllRolesUserByCodeUser(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("select role.id_role, role.short_name, userRole.picked   " +
                "from qldt_user qldtUser " +
                "      inner join user_role userRole on qldtUser.id_user = userRole.id_user   " +
                "      inner join role role on userRole.id_role = role.id_role   " +
                "where qldtUser.code_user = :codeUser ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        List<Object[]> result = query.getResultList();
        List<FindAllRolesUserResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                FindAllRolesUserResponse response = new FindAllRolesUserResponse();
                response.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
                response.setRole(ValueUtil.getStringByObject(obj[1]));
                response.setPicked(ValueUtil.getIntegerByObject(obj[2]));
                responses.add(response);
            }
        }
        return responses;
    }

    @Override
    public List<UserRole> findUserRoleByCodeUser(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("select userRole.id_user_role, userRole.id_user, userRole.id_role,   " +
                "        userRole.time_created, userRole.time_modified,   " +
                "        userRole.picked   " +
                " from qldt_user qldtUser   " +
                "     inner join user_role userRole on qldtUser.id_user = userRole.id_user   " +
                "     inner join role role on userRole.id_role = role.id_role   " +
                " where qldtUser.code_user = :codeUser  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        List<Object[]> result = query.getResultList();
        List<UserRole> userRoles = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result){
                UserRole userRole = new UserRole();
                userRole.setIdUserRole(ValueUtil.getIntegerByObject(obj[0]));
                userRole.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                userRole.setIdRole(ValueUtil.getIntegerByObject(obj[2]));
                userRole.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                userRole.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                userRole.setPicked(ValueUtil.getIntegerByObject(obj[5]));
                userRoles.add(userRole);
            }
        }
        return userRoles;
    }

    @Override
    public Optional<UserRole> findUserRoleByIdUserRole(Integer idUserRole) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_user_role, id_user, id_role, " +
                "       time_created, time_modified, picked " +
                " from user_role userRole " +
                "where userRole.id_user_role = :idUserRole ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idUserRole", idUserRole);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj:result){
                UserRole userRole = new UserRole();
                userRole.setIdUserRole(ValueUtil.getIntegerByObject(obj[0]));
                userRole.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                userRole.setIdRole(ValueUtil.getIntegerByObject(obj[2]));
                userRole.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                userRole.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                userRole.setPicked(ValueUtil.getIntegerByObject(obj[5]));
                return Optional.of(userRole);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserRole> findUserRoleByIdUserRoleAndIdUser(Integer idUserRole, Integer idUser) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id_user_role, id_user, id_role, " +
                "       time_created, time_modified, picked   " +
                "  from user_role userRole   " +
                "  where userRole.id_user_role != :idUserRole   " +
                "  and userRole.id_user = :idUser LIMIT 1  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idUserRole", idUserRole);
        query.setParameter("idUser", idUser);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj:result){
                UserRole userRole = new UserRole();
                userRole.setIdUserRole(ValueUtil.getIntegerByObject(obj[0]));
                userRole.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                userRole.setIdRole(ValueUtil.getIntegerByObject(obj[2]));
                userRole.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                userRole.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                userRole.setPicked(ValueUtil.getIntegerByObject(obj[5]));
                return Optional.of(userRole);
            }
        }
        return Optional.empty();
    }

    @Override
    public DepartmentUserRoleDto getDepartmentCurrentUserRoleDtoByCodeUser(String codeUser) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.title, userRole.id_user_role " +
                "from ktx_user ktxUser " +
                "    inner join user_role userRole on ktxUser.id_ktx_user = userRole.id_user " +
                "    left join department de on userRole.id_department = de.id_department " +
                "where userRole.picked = :isPicked " +
                "and ktxUser.code_user = :codeUser ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeUser", codeUser);
        query.setParameter("isPicked", Constants.ROLE_USER_PICKED);
        List<Object[]> result = query.getResultList();
        DepartmentUserRoleDto departmentUserRoleDto = new DepartmentUserRoleDto();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                departmentUserRoleDto.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
                departmentUserRoleDto.setNameDepartment(ValueUtil.getStringByObject(obj[1]));
                departmentUserRoleDto.setIdUserRole(ValueUtil.getIntegerByObject(obj[2]));
            }
        }
        return departmentUserRoleDto;
    }

    @Override
    public List<UserRole> findUserRoleByIdRole(Integer idRole) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id_user_role,     " +
                "         id_user,     " +
                "         id_role,     " +
                "         time_created,     " +
                "         time_modified,     " +
                "         picked     " +
                "  from user_role userRole     " +
                "  where userRole.id_role = :idRole ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRole", idRole);
        List<UserRole> userRoles = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj:result){
                UserRole userRole = new UserRole();
                userRole.setIdUserRole(ValueUtil.getIntegerByObject(obj[0]));
                userRole.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                userRole.setIdRole(ValueUtil.getIntegerByObject(obj[2]));
                userRole.setTimeCreated(ValueUtil.getLongByObject(obj[3]));
                userRole.setTimeModified(ValueUtil.getLongByObject(obj[4]));
                userRole.setPicked(ValueUtil.getIntegerByObject(obj[5]));
                userRoles.add(userRole);
            }
        }
        return userRoles;
    }

    @Override
    public List<UserRoleDto> findUserRoleByNameRoleAndIdDepartment(String nameRole, Integer department) {
        StringBuilder sb = new StringBuilder();
        sb.append("select  userRole.id_user_role, userRole.id_user, userRole.id_role, " +
                "         userRole.time_created, userRole.time_modified, " +
                "          userRole.picked, qldtUser.user_name " +
                "from qldt_user qldtUser " +
                "         inner join user_role userRole on qldtUser.id_user = userRole.id_user " +
                "         inner join role role on userRole.id_role = role.id_role  " +
                "where role.title = :roleTitle ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("roleTitle", nameRole);
        List<UserRoleDto> userRoles = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                UserRoleDto userRole = new UserRoleDto();
                userRole.setIdUserRole(ValueUtil.getIntegerByObject(obj[0]));
                userRole.setIdUser(ValueUtil.getIntegerByObject(obj[1]));
                userRole.setIdRole(ValueUtil.getIntegerByObject(obj[2]));
                userRole.setTimeCreated(ValueUtil.getStringByObject(obj[3]));
                userRole.setTimeModified(ValueUtil.getStringByObject(obj[4]));
                userRole.setPicked(ValueUtil.getIntegerByObject(obj[5]));
                userRole.setUserName(ValueUtil.getStringByObject(obj[6]));
                userRoles.add(userRole);
            }
        }
        return userRoles;
    }

}
