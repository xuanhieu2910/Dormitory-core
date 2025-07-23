package teamit.hust.ktxcdshustbe.repository.batchesRegistrationSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationSchedule;

@Repository
public interface BatchesRegistrationScheduleRepository extends JpaRepository<BatchesRegistrationSchedule, Integer>, BatchesRegistrationScheduleRepositoryCustom {
}
