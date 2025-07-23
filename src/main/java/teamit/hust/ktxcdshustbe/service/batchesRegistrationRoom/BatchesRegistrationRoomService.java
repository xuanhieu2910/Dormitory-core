package teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom;

import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;

import java.util.List;

public interface BatchesRegistrationRoomService {

    List<BatchesRegistrationRoom> saveAll(List<BatchesRegistrationRoom> batchesRegistrationRoomList);

    List<BatchesRegistrationRoom> findAllBatchesRegistrationRoomByCodeBatchesRegistration(String codeBatchesRegistration);

    void deleteAll(List<BatchesRegistrationRoom> batchesRegistrationRoomsDelete);
}
