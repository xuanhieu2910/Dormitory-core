package teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.batchesRegistrationRoom.FindAllBatchesRegistrationRoomDto;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistrationRoom;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistrationRoom.BatchesRegistrationRoomRepository;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.FindAllBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.response.batchesRegistrationRoom.FindAllBatchesRegistrationRoomResponse;
import teamit.hust.ktxcdshustbe.service.batchesRegistrationRoom.BatchesRegistrationRoomService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.ArrayList;
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

    @Override
    public Page<FindAllBatchesRegistrationRoomResponse>
    findAllBatchesRegistrationRoom(FindAllBatchesRegistrationRoomRequest request) {
        verifyFindAllBatchesRegistrationRoom(request);
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllBatchesRegistrationRoomDto> dtos = batchesRegistrationRoomRepository.findAllBatchesRegistrationRoom(pageable, request);
        return new PageImpl<>(convertToFindAllBatchesRegistrationRoom(dtos.getContent()), pageable, dtos.getTotalElements());
    }

    private List<FindAllBatchesRegistrationRoomResponse> convertToFindAllBatchesRegistrationRoom(List<FindAllBatchesRegistrationRoomDto> contents) {
        List<FindAllBatchesRegistrationRoomResponse> responses = new ArrayList<>();
        for (FindAllBatchesRegistrationRoomDto data: contents){
            FindAllBatchesRegistrationRoomResponse response = new FindAllBatchesRegistrationRoomResponse();
            response.setTitleRoom(data.getTitle());
            response.setCodeRoom(data.getCodeRoom());
            response.setGender(data.getSexRoom().equals(Constants.FEMALE) ? Constants.TITLE_SEX[0] : Constants.TITLE_SEX[1]);
            response.setPrice(data.getPrice());
            response.setTimeCreated(data.getTimeCreated());
            response.setTimeModified(data.getTimeModified());
            response.setStatus(data.getIsActive());
            response.setLimitAmountPeople(data.getLimitAmountPeople());
            response.setQuantityHired(data.getQuantityHired());
            response.setRemainAmount(data.getRemainAmount());
            response.setLimitAmountPeopleRegister(data.getLimitAmountPeopleRegister());
            response.setQuantityRegistered(data.getQuantityRegistered());
            response.setRemainAmountRegister(data.getRemainAmountRegister());
            responses.add(response);
        }
        return responses;
    }

    private void verifyFindAllBatchesRegistrationRoom(FindAllBatchesRegistrationRoomRequest request) {
        if (StringUtils.isBlank(request.getCodeBatchesRegisterRoom()) || StringUtils.isBlank(request.getCodeDepartment())){
            throw new ValidParametersException();
        }
    }
}
