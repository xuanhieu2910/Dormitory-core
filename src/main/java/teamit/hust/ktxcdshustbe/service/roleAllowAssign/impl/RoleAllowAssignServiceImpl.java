package teamit.hust.ktxcdshustbe.service.roleAllowAssign.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.RoleAllowAssign;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.roleAllowAssign.RoleAllowAssignRepository;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.FindRestRoleRequest;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.ListDestinationRoleAssignRequest;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.UpdateRoleAllowAssignRequest;
import teamit.hust.ktxcdshustbe.request.roleAllowAssignt.UpdateRoleAllowDataRequest;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindAllRoleAllowAssignResponse;
import teamit.hust.ktxcdshustbe.response.roleAllowAssign.FindRestRoleResponse;
import teamit.hust.ktxcdshustbe.service.roleAllowAssign.RoleAllowAssignService;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleAllowAssignServiceImpl implements RoleAllowAssignService {

    @Autowired
    RoleAllowAssignRepository roleAllowAssignRepository;


    @Override
    public List<FindAllRoleAllowAssignResponse> findAllRoleAllowAssign() {
        return roleAllowAssignRepository.findAllRoleAllowAssignResponse();
    }

    @Override
    public void updateRoleAllowAssign(UpdateRoleAllowDataRequest request){
        List<RoleAllowAssign> roleAllowAssigns = roleAllowAssignRepository.findAllRoleAllowAssign();
        if (CollectionUtils.isEmpty(roleAllowAssigns)) {
            throw new NotFoundException();
        }
        List<UpdateRoleAllowAssignRequest> data = request.getData();
        for (RoleAllowAssign allowAssign : roleAllowAssigns){
            for (UpdateRoleAllowAssignRequest rq : data) {
                if (allowAssign.getIdRole().equals(rq.getIdSourceRoleAssign())) {
                    for (ListDestinationRoleAssignRequest drq : rq.getDestinationRoleAssign()) {
                        if (allowAssign.getAllowAssign().equals(drq.getIdDestinationRoleAssign())) {
                            allowAssign.setStatus(drq.getStatus());
                        }
                    }
                }
            }
        }
        roleAllowAssignRepository.saveAll(roleAllowAssigns);
    }

    @Override
    public void saveAllRoleAllowAssign(List<RoleAllowAssign> roleAllowAssigns) {
        roleAllowAssignRepository.saveAll(roleAllowAssigns);
    }


    @Override
    public Page<FindRestRoleResponse> findRestRoleResponseAssign(FindRestRoleRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roles = new ArrayList<>(ktxUser.getRole());
        Integer idRoleCurrent = roles.get(0).getIdRole();
        return roleAllowAssignRepository.findRestRoleAssignResponse(pageable, request, idRoleCurrent);
    }

    @Override
    public void deleteRoleAssignByIdRole(Integer roleId) {
        roleAllowAssignRepository.deleteRoleAssignByIdRole(roleId);
    }

}
