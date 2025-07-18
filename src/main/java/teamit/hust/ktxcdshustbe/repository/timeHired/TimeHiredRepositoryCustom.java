package teamit.hust.ktxcdshustbe.repository.timeHired;

import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;

import java.util.Optional;

public interface TimeHiredRepositoryCustom {

    Optional<TimeHiredResponse> getTimeHiredActiveResponse();
    Optional<TimeHired> findTimeHiredByTimeHiredId(Integer timeHiredId);
}
