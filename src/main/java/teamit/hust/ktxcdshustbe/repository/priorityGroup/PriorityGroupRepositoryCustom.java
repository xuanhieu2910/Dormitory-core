package teamit.hust.ktxcdshustbe.repository.priorityGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindAllPriorityGroupDto;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindPriorityGroupDetailDto;
import teamit.hust.ktxcdshustbe.request.priorityGroup.FindAllPriorityGroupRequest;

public interface PriorityGroupRepositoryCustom {
    Page<FindAllPriorityGroupDto> findAllPriorityGroup(FindAllPriorityGroupRequest request, Pageable pageable);

    FindPriorityGroupDetailDto getPriorityGroupDetailByPriorityGroupCode(String priorityGroupCode);

}
