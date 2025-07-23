package teamit.hust.ktxcdshustbe.repository.batchesYearGroupRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.BatchesYearGroupRegistration;

@Repository
public interface BatchesYearGroupRegistrationRepository extends JpaRepository<BatchesYearGroupRegistration, Integer>,
        BatchesYearGroupRegistrationRepositoryCustom{
}
