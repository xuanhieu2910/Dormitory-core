package teamit.hust.ktxcdshustbe.repository.batchesRegistration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistration;

@Repository
public interface BatchesRegistrationRepository extends JpaRepository<BatchesRegistration, Integer>, BatchesRegistrationRepositoryCustom {
}
