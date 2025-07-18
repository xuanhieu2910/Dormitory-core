package teamit.hust.ktxcdshustbe.service.role.impl;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.RoleAllowAssign;
import teamit.hust.ktxcdshustbe.entity.RoleCapabilities;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.role.RoleRepository;
import teamit.hust.ktxcdshustbe.request.role.CreateNewRoleRequest;
import teamit.hust.ktxcdshustbe.request.role.FindAllRoleRequest;
import teamit.hust.ktxcdshustbe.request.role.UpdateRoleRequest;
import teamit.hust.ktxcdshustbe.request.roleCapabilities.CreateNewRoleCapabilitiesRequest;
import teamit.hust.ktxcdshustbe.request.roleCapabilities.UpdateRoleCapabilitiesRequest;
import teamit.hust.ktxcdshustbe.response.role.FindAllRoleResponse;
import teamit.hust.ktxcdshustbe.response.role.FindDetailsRoleCapabilitiesResponse;
import teamit.hust.ktxcdshustbe.service.role.RoleService;
import teamit.hust.ktxcdshustbe.service.roleAllowAssign.RoleAllowAssignService;
import teamit.hust.ktxcdshustbe.service.roleCapabilities.RoleCapabilitiesService;
import teamit.hust.ktxcdshustbe.service.userRole.UserRoleService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RoleAllowAssignService roleAllowAssignService;
    @Autowired
    RoleCapabilitiesService roleCapabilitiesService;
    @Autowired
    UserRoleService userRoleService;



    @Override
    public Page<FindAllRoleResponse> findAllRole(FindAllRoleRequest findAllRoleRequest) {
        Pageable pageable = PageUtils.buildPage(findAllRoleRequest.getPage(), findAllRoleRequest.getSize());
        Page<Role> roles = roleRepository.findAllRole(pageable, findAllRoleRequest);
        return new PageImpl<>(convertToFindAllRoleResponse(roles.getContent()),pageable, roles.getTotalElements());
    }

    private List<FindAllRoleResponse> convertToFindAllRoleResponse(List<Role> roles) {
        List<FindAllRoleResponse> responses = new ArrayList<>();
        for (Role role: roles){
            FindAllRoleResponse response = new FindAllRoleResponse();
            response.setIdRole(role.getIdRole());
            response.setTitleRole(role.getTitle());
            response.setContentName(role.getContent());
            response.setDescription(role.getDescription());
            response.setStatus(role.getStatus());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void createNewRole(CreateNewRoleRequest request){
        validateCreateNewRole(request);
        Role role = constructionRole(request);
        roleRepository.save(role);
        createRoleCapabilities(role, request);
        createRoleAllowAssign(role);

    }

    @Override
    public List<Role> findRoleByIds(List<Integer> ids) {
        List<Role> roles = roleRepository.findRolesByIds(ids);
        if (roles.size() != ids.size()) {
            throw new NotFoundException();
        }
        return roles;
    }

    @Override
    public void deleteRole(Integer idRole){
        Optional<Role> role = roleRepository.findByIdRole(idRole);
        if (role.isEmpty()) {
            throw new NotFoundException();
        } else {
            if (role.get().getStatus().equals(Constants.ROLE_DEFAULT)){
                throw new ValidParametersException();
            }
        }
        roleRepository.delete(role.get());
        roleCapabilitiesService.deleteRoleCapabilitiesByIdRole(idRole);
        roleAllowAssignService.deleteRoleAssignByIdRole(idRole);
        userRoleService.deleteUserRoleByIdRole(idRole);
    }

    @Override
    public FindDetailsRoleCapabilitiesResponse findDetailsRoleCapabilitiesByIdRole(Integer idRole) {
        return roleRepository.findDetailsRoleCapabilitiesByIdRole(idRole);
    }

    @Override
    public void updateRole(UpdateRoleRequest request) {
        Optional<Role> role = roleRepository.findByIdRole(request.getIdRole());
        if (role.isEmpty()) {
            throw new NotFoundException();
        }
        validateDataUpdateRole(request,role.get());
        List<RoleCapabilities> roleCapabilitiesList =
                roleCapabilitiesService.findAllRoleCapabilitiesByIdRole(request.getIdRole());
        if (roleCapabilitiesList.size() != request.getCapabilities().size()){
            throw new ValidParametersException();
        }
        updateDataRole(request, role.get());
        updateDataRoleCapabilities(roleCapabilitiesList, request.getCapabilities());
    }

    @Override
    public List<Role> getAllRoleByStatus(Integer status) {
        return roleRepository.findAllRolesByStatus(status);
    }

    @Override
    public Role findRoleByTitleRole(String name) {
        Optional<Role> role = roleRepository.findByTitleRole(name.trim());
        if (role.isEmpty()){
            throw new NotFoundException();
        }
        return role.get();
    }

    private void updateDataRoleCapabilities(List<RoleCapabilities> roleCapabilitiesList,
                                            List<UpdateRoleCapabilitiesRequest> roleCapabilitiesRequest) {
        Long timeCurrent = new Date().getTime();
        Integer idUserModified = ((KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getIdKtxUser();
        for (RoleCapabilities roleCapabilities : roleCapabilitiesList){
            for (UpdateRoleCapabilitiesRequest updateRoleCapabilitiesRequest : roleCapabilitiesRequest){
                if (updateRoleCapabilitiesRequest.getIdCapability().equals(roleCapabilities.getIdCapabilities())){
                    roleCapabilities.setPermission(updateRoleCapabilitiesRequest.getStatus());
                    roleCapabilities.setTimeModified(timeCurrent);
                    roleCapabilities.setIdUserModified(idUserModified);
                    break;
                }
            }
        }
        roleCapabilitiesService.saveAllRoleCapabilities(roleCapabilitiesList);
    }

    private void updateDataRole(UpdateRoleRequest request, Role role) {
        role.setShortName(request.getShortName());
        role.setDescription(request.getDescription());
        role.setStatus(request.getStatus());
        role.setTimeModified(new Date().getTime());
        roleRepository.save(role);
    }

    private void validateDataUpdateRole(UpdateRoleRequest request, Role role){
        if (StringUtils.isBlank(request.getShortName())){
            throw new ValidParametersException();
        }
        if (!role.getShortName().equals(request.getShortName())) {
            Optional<Role> roleOptional = roleRepository.findByShortNameRole(request.getShortName());
            if (roleOptional.isPresent()) {
                throw new ValidParametersException();
            }
        }
        if (!request.getStatus().equals(Constants.ROLE_STATUS_ACTIVE) &&
                !request.getStatus().equals(Constants.ROLE_STATUS_IN_ACTIVE)) {
            throw new ValidParametersException();
        }
    }

    private void createRoleCapabilities(Role role, CreateNewRoleRequest request) {
        List<RoleCapabilities> roleCapabilitiesList = new ArrayList<>();
        Long currentTime = new Date().getTime();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (CreateNewRoleCapabilitiesRequest roleCapabilitiesRequest : request.getCapabilities()){
            RoleCapabilities capabilities = new RoleCapabilities();
            capabilities.setIdRole(role.getIdRole());
            capabilities.setIdCapabilities(roleCapabilitiesRequest.getIdCapability());
            capabilities.setPermission(roleCapabilitiesRequest.getStatus());
            capabilities.setTimeCreated(currentTime);
            capabilities.setTimeModified(currentTime);
            capabilities.setIdUserModified(ktxUser.getIdKtxUser());
            roleCapabilitiesList.add(capabilities);
        }
        roleCapabilitiesService.saveAllRoleCapabilities(roleCapabilitiesList);
    }

    private void createRoleAllowAssign(Role currentRole) {
        List<Role> restRole = roleRepository.findRestRoleWithoutCurrentRole(currentRole);
        createSourceAllowAssign(restRole, currentRole.getIdRole());
        createDestinationRoleAllowAssign(restRole, currentRole.getIdRole());
    }

    private void createDestinationRoleAllowAssign(List<Role> restRole, Integer idRole) {
        List<RoleAllowAssign>  destinationRoleAllowAssign = new ArrayList<>();
        for (Role role : restRole){
            RoleAllowAssign allowAssign = new RoleAllowAssign();
            allowAssign.setIdRole(idRole);
            allowAssign.setStatus(Constants.ROLE_ALLOW_ASSIGN_UN_STATUS);
            allowAssign.setAllowAssign(role.getIdRole());
            destinationRoleAllowAssign.add(allowAssign);
        }
        roleAllowAssignService.saveAllRoleAllowAssign(destinationRoleAllowAssign);
    }

    private void createSourceAllowAssign(List<Role> restRole, Integer idRole) {
        List<RoleAllowAssign>  sourceAllowAssign = new ArrayList<>();
        for (Role role : restRole){
            RoleAllowAssign allowAssign = new RoleAllowAssign();
            allowAssign.setIdRole(role.getIdRole());
            allowAssign.setStatus(Constants.ROLE_ALLOW_ASSIGN_UN_STATUS);
            allowAssign.setAllowAssign(idRole);
            sourceAllowAssign.add(allowAssign);
        }
        sourceAllowAssign.add(addRoleCurrentByIdRole(idRole));
        roleAllowAssignService.saveAllRoleAllowAssign(sourceAllowAssign);
    }

    private RoleAllowAssign addRoleCurrentByIdRole(Integer idRole) {
        RoleAllowAssign allowAssign = new RoleAllowAssign();
        allowAssign.setIdRole(idRole);
        allowAssign.setStatus(Constants.ROLE_ALLOW_ASSIGN_UN_STATUS);
        allowAssign.setAllowAssign(idRole);
        return allowAssign;
    }

    private Role constructionRole(CreateNewRoleRequest request) {
        Role role = new Role();
        role.setTitle(request.getTitle());
        role.setStatus(request.getStatus());
        role.setContent(request.getContent());
        role.setShortName(request.getShortName());
        role.setDescription(request.getDescription());
        Long timeCurrent = new Date().getTime();
        role.setTimeCreated(timeCurrent);
        role.setTimeModified(timeCurrent);
        return role;
    }

    private void validateCreateNewRole(CreateNewRoleRequest request){
        if (roleRepository.findRoleByTitleOrShortName(request.getTitle(), request.getShortName()).isPresent()) {
            throw new ExitsObjectException();
        }
        if (!request.getStatus().equals(Constants.ROLE_STATUS_ACTIVE) &&
                !request.getStatus().equals(Constants.ROLE_STATUS_IN_ACTIVE))   {
            throw new ValidParametersException();
        }
    }

}
