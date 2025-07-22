package teamit.hust.ktxcdshustbe.service.priorityGroup;

import teamit.hust.ktxcdshustbe.entity.PriorityGroup;

public interface PriorityGroupService {
    PriorityGroup findPriorGroupByTitle(String titlePriorityGroup);

    PriorityGroup findPriorGroupByIdPriorGroup(Integer idPriorityGroup);
}
