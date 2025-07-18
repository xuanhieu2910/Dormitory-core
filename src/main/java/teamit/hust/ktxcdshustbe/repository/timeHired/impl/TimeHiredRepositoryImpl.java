package teamit.hust.ktxcdshustbe.repository.timeHired.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepositoryCustom;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.List;
import java.util.Optional;

public class TimeHiredRepositoryImpl implements TimeHiredRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<TimeHiredResponse> getTimeHiredActiveResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select timeHired.id_time_hired, sem.title,  " +
                "        timeHired.time_started, timeHired.time_ended    " +
                " from time_hired timeHired    " +
                "     inner join semester sem on timeHired.id_semester = sem.id_semester    " +
                " where timeHired.status = :statusTimeHired ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("statusTimeHired", Constants.TIME_HIRED_STATUS_ACTIVE);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                TimeHiredResponse hired = new TimeHiredResponse();
                hired.setIdTimeHired(ValueUtil.getIntegerByObject(obj[0]));
                hired.setTitleSemester(ValueUtil.getStringByObject(obj[1]));
                hired.setTimeHiredStarted(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[2]), DateUtil.DDMMYYYY));
                hired.setTimeHiredEnded(DateUtil.formatToPattern(ValueUtil.getDateByObject(obj[3]),DateUtil.DDMMYYYY));
                return Optional.of(hired);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<TimeHired> findTimeHiredByTimeHiredId(Integer timeHiredId) {
        StringBuilder sb = new StringBuilder();
        sb.append("select timeHired.id_time_hired, timeHired.time_started,  " +
                "       timeHired.time_ended, timeHired.status, timeHired.time_created,  " +
                "       timeHired.time_modified, timeHired.id_user_created, timeHired.id_user_modified     " +
                "from time_hired timeHired     " +
                "where timeHired.id_time_hired = :timeHiredId ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("timeHiredId", timeHiredId);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result){
                return Optional.of(writeTimeHired(obj));
            }
        }
        return Optional.empty();
    }

    private TimeHired writeTimeHired(Object[] obj){
        TimeHired timeHired = new TimeHired();
        timeHired.setIdTimeHired(ValueUtil.getIntegerByObject(obj[0]));
        timeHired.setTimeStarted(ValueUtil.getLongByObject(obj[1]));
        timeHired.setTimeEnded(ValueUtil.getLongByObject(obj[2]));
        timeHired.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        timeHired.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        timeHired.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        timeHired.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        timeHired.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        return timeHired;
    }
}
