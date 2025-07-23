package teamit.hust.ktxcdshustbe.repository.priorityGroup;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindAllPriorityGroupDto;
import teamit.hust.ktxcdshustbe.dto.priorityGroup.FindPriorityGroupDetailDto;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.request.priorityGroup.FindAllPriorityGroupRequest;

import java.util.List;
import java.util.Optional;

public interface PriorityGroupRepositoryCustom {
    Optional<PriorityGroup> findPriorityGroupByTitle(String titlePriorityGroup);

    Optional<PriorityGroup> findPriorityGroupByIdPriorityGroup(Integer idPriorityGroup);
    Page<FindAllPriorityGroupDto> findAllPriorityGroup(FindAllPriorityGroupRequest request, Pageable pageable);

    FindPriorityGroupDetailDto getPriorityGroupDetailByPriorityGroupCode(String priorityGroupCode);


    Optional<List<PriorityGroup>> findPriorityGroupByListPriorityGroupCode(List<String> codesPriorityGroup);

    Optional<List<PriorityGroup>> findPriorityGroupByIdsPriorityGroup(List<Integer> idsPriorityGroup);
}
