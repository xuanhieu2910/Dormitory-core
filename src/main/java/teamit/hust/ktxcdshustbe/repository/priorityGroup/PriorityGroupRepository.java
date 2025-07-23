package teamit.hust.ktxcdshustbe.repository.priorityGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;

@Repository
public interface PriorityGroupRepository extends JpaRepository<PriorityGroup, Integer>, PriorityGroupRepositoryCustom {
}
