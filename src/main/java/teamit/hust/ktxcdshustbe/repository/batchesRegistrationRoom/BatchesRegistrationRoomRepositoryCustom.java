package teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.batchesRegistrationRoom.FindAllBatchesRegistrationRoomDto;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.FindAllBatchesRegistrationRoomRequest;

import java.util.List;
import java.util.Optional;

public interface BatchesRegistrationRoomRepositoryCustom {
    Optional<List<BatchesRegistrationRoom>> findAllBatchesRegistrationRoomByCodeBatchesRegistrationCustom(String codeBatchesRegistration);

    Page<FindAllBatchesRegistrationRoomDto> findAllBatchesRegistrationRoom(Pageable pageable, FindAllBatchesRegistrationRoomRequest request);
}
