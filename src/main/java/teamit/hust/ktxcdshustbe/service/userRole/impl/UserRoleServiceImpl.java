package teamit.hust.ktxcdshustbe.service.userRole.impl;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.userRole.UserRoleDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.UserRole;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.role.RoleRepository;
import teamit.hust.ktxcdshustbe.repository.user.KtxUserRepository;
import teamit.hust.ktxcdshustbe.repository.userRole.UserRoleRepository;
import teamit.hust.ktxcdshustbe.request.userRole.AddNewRoleDepartmentUserRequest;
import teamit.hust.ktxcdshustbe.request.userRole.AddNewUserRoleDetailsRequest;
import teamit.hust.ktxcdshustbe.request.userRole.UpdateUserRoleRequest;
import teamit.hust.ktxcdshustbe.response.user.FindAllRolesUserResponse;
import teamit.hust.ktxcdshustbe.service.userRole.UserRoleService;
import teamit.hust.ktxcdshustbe.utility.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    KtxUserRepository qldtUserRepository;

    @Override
    public List<UserRole> findAllUserRoleByIdsAndStatusActive(List<Integer> ids) {
        List<UserRole> listUserRole = userRoleRepository.findAllUserRoleByIdsAndStatusActive(ids);
        if (ids.size() != listUserRole.size()) {
            throw new NotFoundException();
        }
        return listUserRole;
    }

    @Override
    public Role findRoleByUserName(String name){
        Optional<Role> role = userRoleRepository.findByTitleRole(name.trim());
        if (role.isEmpty()){
            throw new NotFoundException();
        }
        return role.get();
    }

    @Override
    public UserRole saveUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public List<FindAllRolesUserResponse> findAllRolesUserByCodeUser(String codeUser) {
        return userRoleRepository.findAllRolesUserByCodeUser(codeUser);
    }

    @Override
    public List<UserRole> findUserRoleByCodeUser(String codeUser){
        List<UserRole> userRoles = userRoleRepository.findUserRoleByCodeUser(codeUser);
        if (CollectionUtils.isEmpty(userRoles)){
            throw new NotFoundException();
        }
        return userRoles;
    }

    @Override
    public List<UserRoleDto> findUserRoleByNameRoleAndIdDepartment(String nameRole, Integer department){
        List<UserRoleDto> userRoles = userRoleRepository.findUserRoleByNameRoleAndIdDepartment(nameRole, department);
        if (CollectionUtils.isEmpty(userRoles)){
            throw new NotFoundException();
        }
        return userRoles;
    }

    @Override
    public void saveAllUserRole(List<UserRole> userRoles) {
        userRoleRepository.saveAll(userRoles);
    }

    @Override
    public void updateUserRole(UpdateUserRoleRequest request){
        Optional<UserRole> userRole = userRoleRepository.findUserRoleByIdUserRole(request.getIdUserRole());
        if (userRole.isEmpty()) {
            throw new NotFoundException();
        }
        userRole.get().setIdRole(request.getIdRole());
        userRole.get().setTimeModified(new Date().getTime());
        userRoleRepository.save(userRole.get());
    }

    @Modifying
    @Transactional
    @Override
    public void deleteUserRole(Integer idUserRole){
        Optional<UserRole> userRole = userRoleRepository.findUserRoleByIdUserRole(idUserRole);
        if (userRole.isEmpty()) {
            throw new NotFoundException();
        }
        Optional<UserRole> userRoleOther = userRoleRepository.findUserRoleByIdUserRoleAndIdUser(idUserRole,userRole.get().getIdUser());
        userRoleOther.ifPresent(role -> {
            role.setPicked(Constants.ROLE_USER_PICKED);
            userRoleRepository.save(role);
        });
        userRoleRepository.delete(userRole.get());

    }

    @Override
    public void createNewUserRole(AddNewRoleDepartmentUserRequest request){
        Optional<KtxUser> user = qldtUserRepository.findByKtxUserCode(request.getCodeUser());
        if (user.isEmpty()){
            throw new NotFoundException();
        }
        storeUserRole(request, user.get());
    }

    @Override
    public void deleteUserRoleByIdRole(Integer idRole) {
        List<UserRole> userRoles = userRoleRepository.findUserRoleByIdRole(idRole);
        if (!CollectionUtils.isEmpty(userRoles)) {
            userRoleRepository.deleteAll(userRoles);
        }
    }

    private void storeUserRole(AddNewRoleDepartmentUserRequest request, KtxUser user) {
        List<UserRole> userRoles = new ArrayList<>();
        Long timeCurrent = new Date().getTime();
        for (AddNewUserRoleDetailsRequest roleDepartment : request.getRoles()){
            UserRole userRole = new UserRole();
            userRole.setIdUser(user.getIdKtxUser());
            userRole.setIdRole(roleDepartment.getIdRole());
            userRole.setPicked(Constants.ROLE_USER_UN_PICKED);
            userRole.setTimeCreated(timeCurrent);
            userRole.setTimeModified(timeCurrent);
            userRoles.add(userRole);
        }
        userRoleRepository.saveAll(userRoles);
    }

}
