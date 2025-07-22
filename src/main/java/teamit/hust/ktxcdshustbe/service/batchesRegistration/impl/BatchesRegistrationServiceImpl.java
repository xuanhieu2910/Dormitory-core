package teamit.hust.ktxcdshustbe.service.batchesRegistration.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.BatchesRegistrationDetailDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.FindAllBatchesRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.batchesYearGroupRegistration.BatchesYearGroupRegistrationDto;
import teamit.hust.ktxcdshustbe.exception.ExitsObjectException;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.batchesRegistration.BatchesRegistrationRepository;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.CreateBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationRoom.CreateBatchesRegistrationRoomRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistrationSchedule.CreateBatchesRegistrationScheduleRequest;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.BatchesRegistrationDetailResponse;
import teamit.hust.ktxcdshustbe.response.batchesRegistration.FindAllBatchesRegistrationResponse;
import teamit.hust.ktxcdshustbe.response.batchesYearGroupRegistration.BatchesYearGroupRegistrationResponse;
import teamit.hust.ktxcdshustbe.service.batchesRegistration.BatchesRegistrationService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.util.*;

@Service
public class BatchesRegistrationServiceImpl implements BatchesRegistrationService {

    @Autowired
    BatchesRegistrationRepository batchesRegistrationRepository;

    @Override
    public Page<FindAllBatchesRegistrationResponse> findAll(FindAllBatchesRegistrationRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllBatchesRegistrationDto> batchesRegistrationDtos = batchesRegistrationRepository.findAllBatchesRegistration(request,pageable);
        return new PageImpl<>(convertToFindAllBatchesRegistration(batchesRegistrationDtos.getContent()), pageable, batchesRegistrationDtos.getTotalElements());
    }

    @Override
    public BatchesRegistrationDetailResponse getDetailBatchesRegistration(String codeBatchesRegistration) {
        verifyGetDetailBatchesRegistration(codeBatchesRegistration);
        Optional<BatchesRegistrationDetailDto> registrationDetailDto = batchesRegistrationRepository.getDetailBatchesRegistration(codeBatchesRegistration);
        if (registrationDetailDto.isEmpty()){
            throw new NotFoundException();
        }
        return convertToGetDetailBatchesRegistration(registrationDetailDto.get());
    }

    @Override
    public void createBatchesRegistration(CreateBatchesRegistrationRequest request) {
        verifyCreateBatchesRegistration(request);
        initializeBatchesRegistration(request);
    }

    private void initializeBatchesRegistration(CreateBatchesRegistrationRequest request) {
    }

    private void verifyCreateBatchesRegistration(CreateBatchesRegistrationRequest request) {
        if (StringUtils.isBlank(request.getTitleBatchesRegistration())
            || StringUtils.isBlank(request.getCodeSemester())
            || ObjectUtils.isEmpty(request.getStartTime())
            || ObjectUtils.isEmpty(request.getEndTime())
            ||  ObjectUtils.isEmpty(request.getIdTimeHired())
            || CollectionUtils.isEmpty(request.getYearGroups())
            || CollectionUtils.isEmpty(request.getBatchesRegistrationSchedule())
            || CollectionUtils.isEmpty(request.getRooms())) {
            throw new ValidParametersException();
        }
        if (hasDuplicationYearGroupCreateBatchesRegistration(request.getYearGroups())){
            throw new ValidParametersException();
        }
        Set<String> seenSchedule = new HashSet<>();
        for (CreateBatchesRegistrationScheduleRequest scheduleRequest : request.getBatchesRegistrationSchedule()){
            if (StringUtils.isBlank(scheduleRequest.getPriorityGroupCode())
                || ObjectUtils.isEmpty(scheduleRequest.getRegistrationStartTime())
                || ObjectUtils.isEmpty(scheduleRequest.getRegistrationEndTime())){
                throw new ValidParametersException();
            }
            if (!seenSchedule.add(scheduleRequest.getPriorityGroupCode())) {
                throw new ValidParametersException();
            }
        }
        Set<String> seenRoom = new HashSet<>();
        for (CreateBatchesRegistrationRoomRequest roomRequest : request.getRooms()){
            if (StringUtils.isBlank(roomRequest.getCodeRoom())) {
                throw new ValidParametersException();
            }
            if (!seenRoom.add(roomRequest.getCodeRoom())) {
                throw new ValidParametersException();
            }
        }
        if (!batchesRegistrationRepository.checkNotExitsBatchesRegistration(request.getYearGroups(),
                request.getCodeSemester(), request.getStartTime(), request.getEndTime())){
            throw new ExitsObjectException();
        }
    }

    private boolean hasDuplicationYearGroupCreateBatchesRegistration(List<String> yearGroups){
        Set<String> seen = new HashSet<>();
        for (String item : yearGroups) {
            if (!seen.add(item)) {
                return true;
            }
        }
        return false;
    }


    private BatchesRegistrationDetailResponse convertToGetDetailBatchesRegistration(BatchesRegistrationDetailDto detailDto) {
        Long timeCurrent = new Date().getTime();
        BatchesRegistrationDetailResponse response = new BatchesRegistrationDetailResponse();
        response.setCodeBatchesRegistration(detailDto.getCodeBatchesRegistration());
        response.setTitleBatchesRegistration(detailDto.getTitleBatchesRegistration());
        response.setTitleSemester(detailDto.getTitleSemester());
        response.setCodeSemester(detailDto.getCodeSemester());
        response.setStartTime(detailDto.getStartTime());
        response.setEndTime(detailDto.getEndTime());
        if (timeCurrent >= response.getStartTime() && timeCurrent <= response.getEndTime()) {
            response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_OPENING);
        } else if (timeCurrent <= response.getStartTime()) {
            response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_NOT_YET_OPEN);
        } else if (timeCurrent >= response.getEndTime()) {
            response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_CLOSED);
        }
        response.setNotes(detailDto.getNotes());
        response.setDescription(detailDto.getDescription());
        List<BatchesYearGroupRegistrationResponse> yearGroupRegistrationResponses = new ArrayList<>();
        for (BatchesYearGroupRegistrationDto batchesYearGroupRegistrationDto : detailDto.getBatchesYearGroupRegistrationDtos()){
            BatchesYearGroupRegistrationResponse yearGroupRegistrationResponse = new BatchesYearGroupRegistrationResponse();
            yearGroupRegistrationResponse.setIdBatchesYearGroupRegistration(batchesYearGroupRegistrationDto.getIdYearGroup());
            yearGroupRegistrationResponse.setIdYearGroup(batchesYearGroupRegistrationDto.getIdYearGroup());
            yearGroupRegistrationResponse.setTitleYearGroup(batchesYearGroupRegistrationDto.getTitleYearGroup());
            yearGroupRegistrationResponse.setStatus(batchesYearGroupRegistrationDto.getStatus());
            yearGroupRegistrationResponses.add(yearGroupRegistrationResponse);
        }
        response.setBatchesYearGroupRegistrationResponseList(yearGroupRegistrationResponses);
        return response;
    }

    private void verifyGetDetailBatchesRegistration(String codeBatchesRegistration) {
        if (StringUtils.isBlank(codeBatchesRegistration)){
            throw new ValidParametersException();
        }
    }

    private List<FindAllBatchesRegistrationResponse> convertToFindAllBatchesRegistration(List<FindAllBatchesRegistrationDto> content) {
        List<FindAllBatchesRegistrationResponse> responses = new ArrayList<>();
        Long timeCurrent = new Date().getTime();
        for (FindAllBatchesRegistrationDto dto : content) {
            List<BatchesYearGroupRegistrationResponse> yearGroupRegistrationResponses = new ArrayList<>();
            FindAllBatchesRegistrationResponse response = new FindAllBatchesRegistrationResponse();
            response.setTitleBatchesRegistration(dto.getTitleBatchesRegistration());
            response.setCodeBatchesRegistration(dto.getCodeBatchesRegistration());
            response.setTitleSemester(dto.getTitleSemester());
            response.setCodeSemester(dto.getCodeSemester());
            response.setStartTime(dto.getStartTime());
            response.setEndTime(dto.getEndTime());
            if (timeCurrent >= response.getStartTime() && timeCurrent <= response.getEndTime()) {
                response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_OPENING);
            } else if (timeCurrent <= response.getStartTime()) {
                response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_NOT_YET_OPEN);
            } else if (timeCurrent >= response.getEndTime()) {
                response.setStatus(Constants.STATUS_BATCHES_REGISTRATION_CLOSED);
            }
            for (BatchesYearGroupRegistrationDto batchesYearGroupRegistrationDto : dto.getBatchesYearGroupRegistrationDtos()) {
                BatchesYearGroupRegistrationResponse groupRegistrationResponse = new BatchesYearGroupRegistrationResponse();
                groupRegistrationResponse.setIdBatchesYearGroupRegistration(batchesYearGroupRegistrationDto.getIdBatchesYearGroupRegistration());
                groupRegistrationResponse.setIdYearGroup(batchesYearGroupRegistrationDto.getIdYearGroup());
                groupRegistrationResponse.setTitleYearGroup(batchesYearGroupRegistrationDto.getTitleYearGroup());
                groupRegistrationResponse.setStatus(batchesYearGroupRegistrationDto.getStatus());
                yearGroupRegistrationResponses.add(groupRegistrationResponse);
            }
            response.setBatchesYearGroupRegistrationResponseList(yearGroupRegistrationResponses);
            responses.add(response);
        }
        return responses;
    }
}
