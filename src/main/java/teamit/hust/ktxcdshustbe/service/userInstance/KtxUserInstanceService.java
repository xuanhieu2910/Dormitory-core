package teamit.hust.ktxcdshustbe.service.userInstance;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.KtxUserInstance;
import teamit.hust.ktxcdshustbe.request.userInstance.FindAllUserInstanceRequest;
import teamit.hust.ktxcdshustbe.request.userInstance.UserDetailsInstanceRequest;
import teamit.hust.ktxcdshustbe.request.userInstance.UserInstanceRequest;
import teamit.hust.ktxcdshustbe.response.userInstance.FindAllUserInstanceResponse;

import java.util.List;

public interface KtxUserInstanceService {
    void saveAllData(List<KtxUserInstance> ktxUsersInstance);

    Page<FindAllUserInstanceResponse> findAllUserInstance(FindAllUserInstanceRequest request);

    void UpdateUserInstance(List<UserInstanceRequest> requests);

    void deleteUserInstance(List<UserInstanceRequest> requests);

    void createUserInstance(List<UserDetailsInstanceRequest> requests);
}
