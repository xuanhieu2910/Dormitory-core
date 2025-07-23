package teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom;

import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;

import java.util.List;
import java.util.Optional;

public interface BatchesRegistrationRoomRepositoryCustom {
    Optional<List<BatchesRegistrationRoom>> findAllBatchesRegistrationRoomByCodeBatchesRegistrationCustom(String codeBatchesRegistration);
}
