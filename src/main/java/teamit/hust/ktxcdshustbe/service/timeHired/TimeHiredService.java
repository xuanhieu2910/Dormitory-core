package teamit.hust.ktxcdshustbe.service.timeHired;

import org.springframework.data.domain.Page;
import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.request.timeHired.CreateTimeHiredRequest;
import teamit.hust.ktxcdshustbe.request.timeHired.FindAllTimeHiredRequest;
import teamit.hust.ktxcdshustbe.request.timeHired.UpdateTimeHiredRequest;
import teamit.hust.ktxcdshustbe.response.timeHired.FindAllTimeHiredResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredDetailsResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;

import java.util.Optional;

public interface TimeHiredService {

    TimeHiredResponse getTimeHiredActived();
    TimeHired findTimeHiredById(Integer hiredId);

    Page<FindAllTimeHiredResponse> findAllTimeHired(FindAllTimeHiredRequest request);

    void createTimeHired(CreateTimeHiredRequest request);

    void updateTimeHired(UpdateTimeHiredRequest request);

    void deleteTimeHiredByCode(String codeTimeHired);

    TimeHiredDetailsResponse findDetailsTimeHiredByCode(String codeTimeHired);
}
