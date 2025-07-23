package teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;

@Repository
public interface BatchesRegistrationRoomRepository extends JpaRepository<BatchesRegistrationRoom, Integer>,
        BatchesRegistrationRoomRepositoryCustom {
}
