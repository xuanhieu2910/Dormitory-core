package teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom.BatchesRegistrationRoomRepository;
import teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom.BatchesRegistrationRoomService;

import java.util.List;
import java.util.Optional;

@Service
public class BatchesRegistrationRoomServiceImpl implements BatchesRegistrationRoomService {

    @Autowired
    BatchesRegistrationRoomRepository batchesRegistrationRoomRepository;

    @Override
    public List<BatchesRegistrationRoom> saveAll(List<BatchesRegistrationRoom> batchesRegistrationRoomList) {
        return batchesRegistrationRoomRepository.saveAll(batchesRegistrationRoomList);
    }

    @Override
    public List<BatchesRegistrationRoom> findAllBatchesRegistrationRoomByCodeBatchesRegistration(String codeBatchesRegistration) {
        Optional<List<BatchesRegistrationRoom>> batchesRegistrationRoom =
                batchesRegistrationRoomRepository.findAllBatchesRegistrationRoomByCodeBatchesRegistrationCustom(codeBatchesRegistration);
        if (batchesRegistrationRoom.isEmpty()){
            throw new NotFoundException();
        }
        return batchesRegistrationRoom.get();
    }

    @Override
    public void deleteAll(List<BatchesRegistrationRoom> batchesRegistrationRoomsDelete) {
        batchesRegistrationRoomRepository.deleteAll(batchesRegistrationRoomsDelete);
    }
}
