package teamit.hust.ktxcdshustbe.service.userInstance.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.KtxUserInstance;
import teamit.hust.ktxcdshustbe.entity.Role;
import teamit.hust.ktxcdshustbe.entity.UserRole;
import teamit.hust.ktxcdshustbe.enums.OAuth2Factory;
import teamit.hust.ktxcdshustbe.enums.RolePattern;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.userInstance.KtxUserInstanceRepository;
import teamit.hust.ktxcdshustbe.request.userInstance.FindAllUserInstanceRequest;
import teamit.hust.ktxcdshustbe.request.userInstance.UserDetailsInstanceRequest;
import teamit.hust.ktxcdshustbe.request.userInstance.UserInstanceRequest;
import teamit.hust.ktxcdshustbe.response.userInstance.FindAllUserInstanceResponse;
import teamit.hust.ktxcdshustbe.service.role.RoleService;
import teamit.hust.ktxcdshustbe.service.user.KtxUserService;
import teamit.hust.ktxcdshustbe.service.userInstance.KtxUserInstanceService;
import teamit.hust.ktxcdshustbe.service.userRole.UserRoleService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class KtxUserInstanceServiceImpl implements KtxUserInstanceService {

    @Autowired
    KtxUserInstanceRepository userInstanceRepository;
    @Lazy
    @Autowired
    KtxUserService ktxUserService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;

    @Override
    public void saveAllData(List<KtxUserInstance> ktxUsersInstance) {
        userInstanceRepository.saveAll(ktxUsersInstance);
    }

    @Override
    public Page<FindAllUserInstanceResponse> findAllUserInstance(FindAllUserInstanceRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<KtxUserInstance> result = userInstanceRepository.findAllUserInstance(request,pageable);
        return new PageImpl<>(convertToUserInstance(result), pageable, result.getTotalElements());
    }

    private List<FindAllUserInstanceResponse> convertToUserInstance(Page<KtxUserInstance> result) {
        List<FindAllUserInstanceResponse> userInstances = new ArrayList<>();
        for (KtxUserInstance ktxUserInstance : result) {
            FindAllUserInstanceResponse response = new FindAllUserInstanceResponse();
            response.setIdUserInstance(ktxUserInstance.getIdKtxUserInstance());
            response.setValue(ktxUserInstance.getValue());
            response.setError(ktxUserInstance.getError());
            userInstances.add(response);
        }
        return userInstances;
    }

    @Override
    public void UpdateUserInstance(List<UserInstanceRequest> requests) {
        List<Integer> ids = new ArrayList<>();
        requests.forEach(x->ids.add(x.getIdUserInstance()));
        List<KtxUserInstance> instances = userInstanceRepository.findAllUserInstanceByIds(ids);
        if (instances.size() != ids.size()){
            throw new NotFoundException();
        }
        String timeCurrent = String.valueOf(new Date().getTime());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        for (KtxUserInstance instance : instances){
            for (UserInstanceRequest request : requests){
                if (instance.getIdKtxUserInstance().equals(request.getIdUserInstance())){
                    instance.setValue(request.getValue());
                    instance.setIdUserModified(ktxUser.getIdKtxUser());
                    instance.setTimeModified(timeCurrent);
                    instance.setError(request.getError());
                    break;
                }
            }
        }
        userInstanceRepository.saveAll(instances);
    }

    @Override
    public void deleteUserInstance(List<UserInstanceRequest> requests) {
        List<Integer> ids = new ArrayList<>();
        requests.forEach(x->ids.add(x.getIdUserInstance()));
        List<KtxUserInstance> instances = userInstanceRepository.findAllUserInstanceByIds(ids);
        if (instances.size() != ids.size()){
            throw new NotFoundException();
        }
        userInstanceRepository.deleteAll(instances);
    }

    @Override
    public void createUserInstance(List<UserDetailsInstanceRequest> requests) {
        KtxUser ktxUserCurrent =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        List<KtxUser> ktxUserList= new ArrayList<>();
        for (UserDetailsInstanceRequest request : requests){
            KtxUser ktxUser = new KtxUser();
            ktxUser.setTimeCreated(timeCurrent);
            ktxUser.setTimeModified(timeCurrent);
            ktxUser.setIdUserCreated(ktxUserCurrent.getIdKtxUser());
            ktxUser.setIdUserModified(ktxUserCurrent.getIdKtxUser());
            ktxUser.setTypeLogin(OAuth2Factory.azure.name());
            ktxUser.setCodeUser(String.valueOf(UUID.randomUUID()));
            ktxUser.setValue(request.getValue());
            ktxUser.setUserName(request.getUsername());
            ktxUser.setSex(request.getSex());
            ktxUser.setIsActived(Constants.ACCOUNT_IS_ACTIVED);
            ktxUser.setIdPriorityGroup(request.getIdPriorityGroup());
            ktxUser.setIdYearGroup(request.getIdYearGroup());
            ktxUserList.add(ktxUser);
        }
        ktxUserService.saveAllValue(ktxUserList);
        createUserRole(ktxUserList);
    }
    private void createUserRole(List<KtxUser> ktxUsers) {
        KtxUser ktxUserCurrent =  (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role role = roleService.findRoleByTitleRole(RolePattern.STUDENT.name());
        List<UserRole> userRoles = new ArrayList<>();
        Long currentDate = new Date().getTime();
        for (KtxUser dto : ktxUsers){
            UserRole userRole = new UserRole();
            userRole.setIdUser(dto.getIdKtxUser());
            userRole.setIdRole(role.getIdRole());
            userRole.setTimeCreated(currentDate);
            userRole.setTimeModified(currentDate);
            userRole.setIdUserCreated(ktxUserCurrent.getIdKtxUser());
            userRole.setIdUserModified(ktxUserCurrent.getIdKtxUser());
            userRole.setPicked(Constants.ROLE_USER_PICKED);
            userRoles.add(userRole);
        }
        userRoleService.saveAllUserRole(userRoles);
    }
}
