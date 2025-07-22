package teamit.hust.ktxcdshustbe.service.priorityGroup;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.request.priorityGroup.CreatePriorityGroupRequest;
import teamit.hust.ktxcdshustbe.request.priorityGroup.FindAllPriorityGroupRequest;
import teamit.hust.ktxcdshustbe.request.priorityGroup.UpdatePriorityGroupRequest;
import teamit.hust.ktxcdshustbe.response.priorityGroup.FindAllPriorityGroupResponse;
import teamit.hust.ktxcdshustbe.response.priorityGroup.PriorityGroupDetailResponse;

public interface PriorityGroupService {
    Page<FindAllPriorityGroupResponse> findAllPriorityGroup(FindAllPriorityGroupRequest request);

    PriorityGroup create(CreatePriorityGroupRequest request);

    PriorityGroup update(UpdatePriorityGroupRequest request);

    void delete(String priorityGroupCode);

    PriorityGroupDetailResponse getPriorityGroupDetail(String priorityGroupCode);
}
