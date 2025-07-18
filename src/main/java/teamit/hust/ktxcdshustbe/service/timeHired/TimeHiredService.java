package teamit.hust.ktxcdshustbe.service.timeHired;

import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;

import java.util.Optional;

public interface TimeHiredService {

    TimeHiredResponse getTimeHiredActived();
    TimeHired findTimeHiredById(Integer hiredId);
}
