package teamit.hust.ktxcdshustbe.repository.priorityGroup;

import teamit.hust.ktxcdshustbe.entity.PriorityGroup;

import java.util.Optional;

public interface PriorityGroupRepositoryCustom {
    Optional<PriorityGroup> findPriorityGroupByTitle(String titlePriorityGroup);
}
