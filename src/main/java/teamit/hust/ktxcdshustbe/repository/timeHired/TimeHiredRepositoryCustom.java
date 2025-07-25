package teamit.hust.ktxcdshustbe.repository.timeHired;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamit.hust.ktxcdshustbe.dto.timeHired.FindAllTimeHiredDto;
import teamit.hust.ktxcdshustbe.dto.timeHired.TimeHiredCurrentDto;
import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.request.timeHired.FindAllTimeHiredRequest;
import teamit.hust.ktxcdshustbe.response.timeHired.FindAllTimeHiredResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;

import java.util.Optional;

public interface TimeHiredRepositoryCustom {

    Optional<TimeHiredResponse> getTimeHiredActiveResponse();
    Optional<TimeHired> findTimeHiredByTimeHiredId(Integer timeHiredId);

    Page<FindAllTimeHiredDto> findAllTimeHired(Pageable pageable, FindAllTimeHiredRequest request);

    Optional<TimeHired> findTimeHiredByCodeTimeHired(String codeTimeHired);

    Optional<TimeHired> findTimeHiredCurrent();
}
