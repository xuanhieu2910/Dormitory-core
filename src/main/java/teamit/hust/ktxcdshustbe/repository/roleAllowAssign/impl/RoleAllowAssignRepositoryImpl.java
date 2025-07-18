package teamit.hust.ktxcdshustbe.repository.roleAllowAssign.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.RoleAllowAssign;
import teamit.hust.ktxcdshustbe.enums.RolePattern;
import teamit.hust.ktxcdshustbe.repository.roleAllowAssign.RoleAllowAssignRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.FindRestRoleRequest;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.DestinationRoleAssignResponse;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindAllRoleAllowAssignResponse;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindRestRoleResponse;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.SourceRoleAssignResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleAllowAssignRepositoryImpl implements RoleAllowAssignRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<FindAllRoleAllowAssignResponse> findAllRoleAllowAssignResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append("select role.id_role               idSourceRole, " +
                "       role.short_name            titleSourceRole, " +
                "       destinationRole.id_role    idDestinationRole, " +
                "       destinationRole.short_name titleDestinationRole, " +
                "       roleAllowAssign.status " +
                "from role role " +
                "         inner join role_allow_assign roleAllowAssign on role.id_role = roleAllowAssign.id_role " +
                "         inner join role destinationRole on roleAllowAssign.allow_assign = destinationRole.id_role " +
                "where role.title != :title ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("title", RolePattern.SUPER_ADMIN.name());
        List<Object[]> result = query.getResultList();
        Map<Integer, FindAllRoleAllowAssignResponse> mapRoles = new HashMap<>();
        if (!CollectionUtils.isEmpty(result)){
            Integer keyRole = null;
            for (Object[] obj : result){
                keyRole = ValueUtil.getIntegerByObject(obj[0]);
                if (mapRoles.containsKey(keyRole)) {
                    DestinationRoleAssignResponse desRole = new DestinationRoleAssignResponse();
                    desRole.setIdDestinationRoleAssign(ValueUtil.getIntegerByObject(obj[2]));
                    desRole.setNameDestinationRoleAssign(ValueUtil.getStringByObject(obj[3]));
                    desRole.setStatus(ValueUtil.getIntegerByObject(obj[4]));
                    mapRoles.get(keyRole).getDestinationRoleAssignResponseList().add(desRole);
                } else {
                    FindAllRoleAllowAssignResponse allowAssignResponse = new FindAllRoleAllowAssignResponse();
                    SourceRoleAssignResponse souRole = new SourceRoleAssignResponse();
                    souRole.setIdSourceRoleAssign(ValueUtil.getIntegerByObject(obj[0]));
                    souRole.setNameSourceRoleAssign(ValueUtil.getStringByObject(obj[1]));
                    List<DestinationRoleAssignResponse> desRoles = new ArrayList<>();
                    DestinationRoleAssignResponse desRole = new DestinationRoleAssignResponse();
                    desRole.setIdDestinationRoleAssign(ValueUtil.getIntegerByObject(obj[2]));
                    desRole.setNameDestinationRoleAssign(ValueUtil.getStringByObject(obj[3]));
                    desRole.setStatus(ValueUtil.getIntegerByObject(obj[4]));
                    desRoles.add(desRole);
                    allowAssignResponse.setSourceRoleAssignResponse(souRole);
                    allowAssignResponse.setDestinationRoleAssignResponseList(desRoles);
                    mapRoles.put(keyRole, allowAssignResponse);
                }
            }
        }
        return new ArrayList<>(mapRoles.values());
    }

    @Override
    public List<RoleAllowAssign> findAllRoleAllowAssign() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select rl.id_role_allow_assign, " +
                "       rl.id_role, " +
                "       rl.allow_assign, " +
                "       rl.status " +
                "from role_allow_assign rl  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        List<Object[]> result = query.getResultList();
        List<RoleAllowAssign> roleAllowAssigns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                RoleAllowAssign allowAssign = new RoleAllowAssign();
                allowAssign.setIdRoleAllowAssign(ValueUtil.getIntegerByObject(obj[0]));
                allowAssign.setIdRole(ValueUtil.getIntegerByObject(obj[1]));
                allowAssign.setAllowAssign(ValueUtil.getIntegerByObject(obj[2]));
                allowAssign.setStatus(ValueUtil.getIntegerByObject(obj[3]));
                roleAllowAssigns.add(allowAssign);
            }
        }
        return roleAllowAssigns;
    }

    @Override
    public Page<FindRestRoleResponse> findRestRoleAssignResponse(Pageable pageable,
                                                                 FindRestRoleRequest request,
                                                                 Integer idRoleCurrent) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select result.idDestinationRole, result.titleDestinationRole    " +
                "       from (select destinationRole.id_role    idDestinationRole,    " +
                "                    destinationRole.short_name titleDestinationRole    " +
                "             from role role    " +
                "                      inner join role_allow_assign roleAllowAssign    " +
                "           on role.id_role = roleAllowAssign.id_role    " +
                "                      inner join role destinationRole    " +
                "           on roleAllowAssign.allow_assign = destinationRole.id_role    " +
                "             where role.id_role = :idRole    " +
                "               and roleAllowAssign.status = :status    " +
                "               and destinationRole.id_role not in (select role.id_role    " +
                "       from qldt_user qldtUser    " +
                "                inner join user_role userRole on qldtUser.id_user = userRole.id_user    " +
                "                inner join role role on userRole.id_role = role.id_role    " +
                "where qldtUser.code_user = :codeUser)) as result    " +
                "where 1 = 1  ");
        setConditionFindRestRoleAssign(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindRestRoleAssign(request, query, idRoleCurrent);
        PageUtils.buildPage(request.getPage(), request.getSize());
        List<Object[]> result = query.getResultList();
        List<FindRestRoleResponse> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                FindRestRoleResponse response = new FindRestRoleResponse();
                response.setIdRole(ValueUtil.getIntegerByObject(obj[0]));
                response.setNameRole(ValueUtil.getStringByObject(obj[1]));
                responses.add(response);
            }
        }
        return new PageImpl<>(responses, pageable, countFindRestRoleAssign(request, idRoleCurrent));
    }


    @Transactional
    @Modifying
    @Override
    public void deleteRoleAssignByIdRole(Integer roleId) {
        StringBuilder sb = new StringBuilder();
        sb.append(" delete " +
                "from role_allow_assign " +
                "where id_role = :idRole or allow_assign = :idRole ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idRole", roleId);
        query.executeUpdate();
    }

    private long countFindRestRoleAssign(FindRestRoleRequest request, Integer idRoleCurrent) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(0) count   " +
                "        from (select destinationRole.id_role    idDestinationRole,   " +
                "   destinationRole.short_name titleDestinationRole   " +
                " from role role   " +
                "     inner join role_allow_assign roleAllowAssign   " +
                "       on role.id_role = roleAllowAssign.id_role   " +
                "     inner join role destinationRole   " +
                "       on roleAllowAssign.allow_assign = destinationRole.id_role   " +
                "where role.id_role = :idRole   " +
                "       and roleAllowAssign.status = :status   " +
                "       and destinationRole.id_role not in (select role.id_role   " +
                "       from qldt_user qldtUser   " +
                "       inner join user_role userRole on qldtUser.id_user = userRole.id_user   " +
                "       inner join role role on userRole.id_role = role.id_role   " +
                "       where qldtUser.code_user = :codeUser)) as result   " +
                "where 1 = 1 ");
        setConditionFindRestRoleAssign(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindRestRoleAssign(request, query, idRoleCurrent);
        return ValueUtil.getLongByObject(query.getSingleResult());
    }

    private void setParameterFindRestRoleAssign(FindRestRoleRequest request, Query query, Integer idRoleCurrent) {
        query.setParameter("idRole",idRoleCurrent);
        query.setParameter("status", Constants.ROLE_ALLOW_ASSIGN_STATUS);
        query.setParameter("codeUser", request.getCodeUser());
        if (StringUtils.isNotBlank(request.getKeyword())) {
            query.setParameter("keyword", request.getKeyword());
        }
    }

    private void setConditionFindRestRoleAssign(FindRestRoleRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append(" and (result.titleDestinationRole REGEXP :keyword ) ");
        }
    }

}
