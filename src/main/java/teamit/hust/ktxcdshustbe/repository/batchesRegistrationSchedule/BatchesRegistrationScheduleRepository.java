package teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationSchedule;

@Repository
public interface BatchesRegistrationScheduleRepository extends JpaRepository<BatchesRegistrationSchedule, Integer>, BatchesRegistrationScheduleRepositoryCustom {

    void deleteByIdPriorityGroup(int idPriorityGroup);

    @Modifying
    @Transactional
    @Query("update BatchesRegistrationSchedule b set b.idPriorityGroup = null where b.idPriorityGroup = :id")
    void deleteIdPriorityGroup(@Param("id")int idPriorityGroup);
}
