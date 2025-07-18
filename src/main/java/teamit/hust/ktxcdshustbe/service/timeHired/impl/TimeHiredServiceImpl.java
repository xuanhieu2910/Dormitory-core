package teamit.hust.ktxcdshustbe.service.timeHired.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.exception.ValidParametersException;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepository;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;
import teamit.hust.ktxcdshustbe.service.timeHired.TimeHiredService;
import teamit.hust.ktxcdshustbe.utility.Constants;

import java.lang.module.Configuration;
import java.util.Optional;

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
        if (timeHired.isPresent() && timeHired.get().getStatus().equals(Constants.TIME_HIRED_STATUS_IN_ACTIVE)){
            throw new ValidParametersException();
        }
        return timeHired.get();
    }
}
