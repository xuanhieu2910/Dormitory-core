package teamit.hust.ktxcdshustbe.repository.priorityGroup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;

@Repository
public interface PriorityGroupRepository extends CrudRepository<PriorityGroup, Integer>, PriorityGroupRepositoryCustom {
}
