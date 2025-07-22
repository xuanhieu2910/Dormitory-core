package teamit.hust.ktxcdshustbe.repository.priorityGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.repository.semester.SemesterRepositoryCustom;

import java.util.Optional;

@Repository
public interface PriorityGroupRepository extends JpaRepository<PriorityGroup, Integer>, PriorityGroupRepositoryCustom {
    Optional<PriorityGroup> findByPriorityGroupCode(String priorityGroupCode);

    boolean existsByPriorityGroupCode(String priorityGroupCode);
}
