package teamit.hust.ktxcdshustbe.service.timeHired.impl;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.dto.timeHired.FindAllTimeHiredDto;
import teamit.hust.ktxcdshustbe.dto.timeHired.TimeHiredCurrentDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepository;
import teamit.hust.ktxcdshustbe.request.timeHired.CreateTimeHiredRequest;
import teamit.hust.ktxcdshustbe.request.timeHired.FindAllTimeHiredRequest;
import teamit.hust.ktxcdshustbe.request.timeHired.UpdateTimeHiredRequest;
import teamit.hust.ktxcdshustbe.response.timeHired.FindAllTimeHiredResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredCurrentResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredDetailsResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;
import teamit.hust.ktxcdshustbe.service.timeHired.TimeHiredService;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.PageUtils;

import java.lang.module.Configuration;
import java.sql.Time;
import java.util.*;

@Service
public class TimeHiredServiceImpl implements TimeHiredService {

    @Autowired
    TimeHiredRepository timeHiredRepository;


    @Override
    public TimeHiredResponse getTimeHiredActived() {
        Optional<TimeHiredResponse>timeHiredResponse = timeHiredRepository.getTimeHiredActiveResponse();
        if (timeHiredResponse.isEmpty()) {
            throw new NotFoundException();
        }
        return timeHiredResponse.get();
    }

    @Override
    public TimeHired findTimeHiredById(Integer hiredId) {
        Optional<TimeHired>timeHired = timeHiredRepository.findTimeHiredByTimeHiredId(hiredId);
        if (timeHired.isEmpty()) {
            throw new NotFoundException();
        }
        if (timeHired.get().getStatus().equals(Constants.TIME_HIRED_STATUS_IN_ACTIVE)){
            throw new ValidParametersException();
        }
        return timeHired.get();
    }

    @Override
    public Page<FindAllTimeHiredResponse> findAllTimeHired(FindAllTimeHiredRequest request) {
        Pageable pageable = PageUtils.buildPage(request.getPage(), request.getSize());
        Page<FindAllTimeHiredDto> findAllTimeHiredResponses = timeHiredRepository.findAllTimeHired(pageable,request);
        return new PageImpl<>(convertFindAllTimeHiredResponse(findAllTimeHiredResponses.getContent()), pageable, findAllTimeHiredResponses.getTotalElements());
    }

    @Override
    public void createTimeHired(CreateTimeHiredRequest request) {
        validateDataCreateTimeHired(request);
        timeHiredRepository.save(constructTimeHired(request));
    }

    private TimeHired constructTimeHired(CreateTimeHiredRequest request) {
        TimeHired timeHired = new TimeHired();
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        timeHired.setTitleTimeHired(request.getTitleTimeHired());
        timeHired.setTimeStarted(request.getTimeStart());
        timeHired.setTimeEnded(request.getTimeEnd());
        if(ObjectUtils.isNotEmpty(request.getStatus())){
            timeHired.setStatus(request.getStatus());
        }
        else {
            timeHired.setStatus(Constants.TIME_HIRED_STATUS_IN_ACTIVE);
        }
        timeHired.setTimeCreated(timeCurrent);
        timeHired.setTimeModified(timeCurrent);
        timeHired.setIdUserCreated(ktxUser.getIdKtxUser());
        timeHired.setCodeTimeHired(String.valueOf(UUID.randomUUID()));
        timeHired.setIdUserModified(ktxUser.getIdKtxUser());
        return timeHired;
    }

    private void validateDataCreateTimeHired(CreateTimeHiredRequest request) {
        if (ObjectUtils.isNotEmpty(request.getTimeStart())
            || ObjectUtils.isNotEmpty(request.getTimeEnd())
            || StringUtils.isBlank(request.getTitleTimeHired())) {
            throw new ValidParametersException();
        }
        if (request.getTimeStart() <= request.getTimeEnd()) {
            throw new ValidParametersException();
        }
    }

    @Override
    public void updateTimeHired(UpdateTimeHiredRequest request) {
        TimeHired timeHired = validateDataUpdateTimeHired(request);
        timeHiredRepository.save(editTimeHired(timeHired,request));
    }

    private TimeHired editTimeHired(TimeHired timeHired, UpdateTimeHiredRequest request) {
        if(ObjectUtils.isNotEmpty(request.getTimeStart())){
            timeHired.setTimeStarted(request.getTimeStart());
        }
        if(ObjectUtils.isNotEmpty(request.getTimeEnd())){
            timeHired.setTimeEnded(request.getTimeEnd());
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())) {
            timeHired.setStatus(request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getTitleTimeHired()) &&
                !timeHired.getTitleTimeHired().equals(request.getTitleTimeHired())) {
            timeHired.setTitleTimeHired(request.getTitleTimeHired());
        }
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long timeCurrent = new Date().getTime();
        timeHired.setTimeModified(timeCurrent);
        timeHired.setIdUserModified(ktxUser.getIdKtxUser());
        return timeHired;
    }

    private TimeHired validateDataUpdateTimeHired(UpdateTimeHiredRequest request) {
        Optional<TimeHired> timeHiredOptional = timeHiredRepository.findTimeHiredByCodeTimeHired(request.getCodeTimeHired());
        if (timeHiredOptional.isEmpty()) {
            throw new NotFoundException();
        }
        if (ObjectUtils.isNotEmpty(request.getTimeStart()) && ObjectUtils.isNotEmpty(request.getTimeEnd())
                && request.getTimeStart() >= request.getTimeEnd()){
            throw new NotFoundException();
        }
        return timeHiredOptional.get();
    }

    @Override
    public void deleteTimeHiredByCode(String codeTimeHired) {
        Optional<TimeHired> timeHiredOptional = timeHiredRepository.findTimeHiredByCodeTimeHired(codeTimeHired);
        if (timeHiredOptional.isEmpty()) {
            throw new NotFoundException();
        }
        timeHiredRepository.delete(timeHiredOptional.get());
    }

    @Override
    public TimeHiredDetailsResponse findDetailsTimeHiredByCode(String codeTimeHired) {
        Optional<TimeHired> timeHiredOptional = timeHiredRepository.findTimeHiredByCodeTimeHired(codeTimeHired);
        if (timeHiredOptional.isEmpty()) {
            throw new NotFoundException();
        }
        return convertTimeHiredDetailsResponse(timeHiredOptional.get());
    }

    @Override
    public TimeHiredCurrentResponse getTimeHiredCurrent() {
        Optional<TimeHired> timeHiredOptional = timeHiredRepository.findTimeHiredCurrent();
        if (timeHiredOptional.isEmpty()){
            throw new NotFoundException();
        }
        return convertToTimeHiredCurrentResponse(timeHiredOptional.get());
    }

    private TimeHiredCurrentResponse convertToTimeHiredCurrentResponse(TimeHired timeHired) {
        TimeHiredCurrentResponse response = new TimeHiredCurrentResponse();
        response.setTimeStarted(timeHired.getTimeStarted());
        response.setTimeEnded(timeHired.getTimeEnded());
        return response;
    }

    private TimeHiredDetailsResponse convertTimeHiredDetailsResponse(TimeHired timeHired) {
        TimeHiredDetailsResponse timeHiredDetailsResponse = new TimeHiredDetailsResponse();
        timeHiredDetailsResponse.setCodeTimeHired(timeHired.getCodeTimeHired());
        timeHiredDetailsResponse.setIdTimeHired(timeHired.getIdTimeHired());
        timeHiredDetailsResponse.setTimeStarted(timeHired.getTimeStarted().toString());
        timeHiredDetailsResponse.setTimeEnded(timeHired.getTimeEnded().toString());
        timeHiredDetailsResponse.setTimeHired(timeHired.getTimeStarted() + "_" + timeHired.getTimeEnded());
        timeHiredDetailsResponse.setStatus(timeHired.getStatus());
        timeHiredDetailsResponse.setTimeCreated(timeHired.getTimeCreated().toString());
        timeHiredDetailsResponse.setTimeModified(timeHired.getTimeModified().toString());
        timeHiredDetailsResponse.setIdUserCreated(timeHired.getIdUserCreated());
        timeHiredDetailsResponse.setIdUserModified(timeHired.getIdUserModified());
        return timeHiredDetailsResponse;
    }

    private List<FindAllTimeHiredResponse> convertFindAllTimeHiredResponse(List<FindAllTimeHiredDto> content) {
        List<FindAllTimeHiredResponse> findAllTimeHiredResponses = new ArrayList<>();
        for (FindAllTimeHiredDto findAllTimeHiredDto : content) {
            FindAllTimeHiredResponse findAllTimeHiredResponse = new FindAllTimeHiredResponse();
            findAllTimeHiredResponse.setCodeTimeHired(findAllTimeHiredDto.getCodeTimeHired());
            findAllTimeHiredResponse.setTimeHired(findAllTimeHiredDto.getTimeHired());
            findAllTimeHiredResponse.setStatus(findAllTimeHiredDto.getStatus());
            findAllTimeHiredResponse.setIdTimeHired(findAllTimeHiredDto.getIdTimeHired());
            findAllTimeHiredResponse.setTimeStarted(findAllTimeHiredDto.getTimeStarted());
            findAllTimeHiredResponse.setTimeEnd(findAllTimeHiredDto.getTimeEnd());
            findAllTimeHiredResponse.setTimeCreated(findAllTimeHiredDto.getTimeCreated());
            findAllTimeHiredResponse.setTimeModified(findAllTimeHiredDto.getTimeModified());
            findAllTimeHiredResponse.setIdUserCreated(findAllTimeHiredDto.getIdUserCreated());
            findAllTimeHiredResponse.setIdUserModified(findAllTimeHiredDto.getIdUserModified());
            findAllTimeHiredResponses.add(findAllTimeHiredResponse);
        }
        return findAllTimeHiredResponses;
    }
}
