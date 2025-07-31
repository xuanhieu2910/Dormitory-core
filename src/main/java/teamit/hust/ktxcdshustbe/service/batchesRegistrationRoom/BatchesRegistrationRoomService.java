package teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.FindAllBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.response.batchesRegistrationRoom.FindAllBatchesRegistrationRoomResponse;

import java.util.List;

public interface BatchesRegistrationRoomService {

    List<BatchesRegistrationRoom> saveAll(List<BatchesRegistrationRoom> batchesRegistrationRoomList);

    List<BatchesRegistrationRoom> findAllBatchesRegistrationRoomByCodeBatchesRegistration(String codeBatchesRegistration);

    void deleteAll(List<BatchesRegistrationRoom> batchesRegistrationRoomsDelete);

    Page<FindAllBatchesRegistrationRoomResponse> findAllBatchesRegistrationRoom(FindAllBatchesRegistrationRoomRequest request);
}
