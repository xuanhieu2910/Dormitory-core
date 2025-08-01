package teamit.hust.ktxcdshustbe.repository.timeHired.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.timeHired.FindAllTimeHiredDto;
import teamit.hust.ktxcdshustbe.dto.timeHired.TimeHiredCurrentDto;
import teamit.hust.ktxcdshustbe.entity.KtxUser;
import teamit.hust.ktxcdshustbe.entity.TimeHired;
import teamit.hust.ktxcdshustbe.repository.timeHired.TimeHiredRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.timeHired.FindAllTimeHiredRequest;
import teamit.hust.ktxcdshustbe.response.timeHired.FindAllTimeHiredResponse;
import teamit.hust.ktxcdshustbe.response.timeHired.TimeHiredResponse;
import teamit.hust.ktxcdshustbe.utility.Constants;
import teamit.hust.ktxcdshustbe.utility.DateUtil;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TimeHiredRepositoryImpl implements TimeHiredRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<TimeHiredResponse> getTimeHiredActiveResponse() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select timeHired.id_time_hired,  " +
                "        timeHired.time_started, timeHired.time_ended    " +
                " from time_hired timeHired    " +
                " where timeHired.status = :statusTimeHired ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("statusTimeHired", Constants.TIME_HIRED_STATUS_ACTIVE);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                TimeHiredResponse hired = new TimeHiredResponse();
                hired.setIdTimeHired(ValueUtil.getIntegerByObject(obj[0]));
                hired.setTimeHiredStarted(ValueUtil.getLongByObject(obj[1]));
                hired.setTimeHiredEnded(ValueUtil.getLongByObject(obj[2]));
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
                "       timeHired.time_modified, timeHired.id_user_created, timeHired.id_user_modified, " +
                "       timeHired.code_time_hired,  timeHired.title_time_hired    " +
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

    @Override
    public Page<FindAllTimeHiredDto> findAllTimeHired(Pageable pageable, FindAllTimeHiredRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select time_hired.id_time_hired, time_started, " +
                "       time_ended, status, time_created,  " +
                "       time_modified, id_user_created, id_user_modified,code_time_hired " +
                "from time_hired where 1=1 ");

        if (StringUtils.isNotBlank(request.getKeyword())){
            sb.append("   and (time_hired.time_started REGEXP '[' + :keyword + ']') OR " +
                    "       (time_hired.time_ended REGEXP '[' + :keyword + ']') ");
        }
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(request.getKeyword())) {
            query.setParameter("keyword", request.getKeyword());
        }
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllTimeHiredDto> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result){
                FindAllTimeHiredDto hired = new FindAllTimeHiredDto();
                String timeHiredStart = ValueUtil.getStringByObject(obj[1]);
                String timeHiredEnd = ValueUtil.getStringByObject(obj[2]);
                hired.setTimeHired(timeHiredStart + " - " + timeHiredEnd);
                hired.setIdTimeHired(ValueUtil.getIntegerByObject(obj[0]));
                hired.setTimeStarted(timeHiredStart);
                hired.setTimeEnd(timeHiredEnd);
                hired.setStatus(ValueUtil.getIntegerByObject(obj[3]));
                hired.setTimeCreated(ValueUtil.getStringByObject(obj[4]));
                hired.setTimeModified(ValueUtil.getStringByObject(obj[5]));
                hired.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                hired.setIdUserModified(ValueUtil.getStringByObject(obj[7]));
                hired.setCodeTimeHired(ValueUtil.getStringByObject(obj[8]));
                responses.add(hired);

            }
        }
        return new PageImpl<>(responses, pageable, countTimeHiredDto(request));
    }

    @Override
    public Optional<TimeHired> findTimeHiredByCodeTimeHired(String codeTimeHired) {
        StringBuilder sb = new StringBuilder();
        sb.append("select timeHired.id_time_hired, timeHired.time_started,  " +
                "       timeHired.time_ended, timeHired.status, timeHired.time_created,  " +
                "       timeHired.time_modified, timeHired.id_user_created, timeHired.id_user_modified," +
                "       timeHired.code_time_hired, timeHired.title_time_hired      " +
                "from time_hired timeHired     " +
                "where timeHired.code_time_hired = :codeTimeHired ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeTimeHired", codeTimeHired);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result){
                return Optional.of(writeTimeHired(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<TimeHired> findTimeHiredCurrent() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select th.id_time_hired, th.time_started, th.time_ended,  " +
                "       th.status, th.time_created, th.time_modified,   " +
                "       th.id_user_created, th.id_user_modified, th.code_time_hired, th.title_time_hired  " +
                "from batches_registration br  " +
                "         inner join batches_year_group_registration bygr  " +
                "                    on br.id_batches_registration = bygr.id_batches_registration  " +
                "         inner join year_group yg on bygr.id_year_group = yg.id_year_group  " +
                "         inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration  " +
                "         inner join priority_group pg on brs.id_priority_group = pg.id_priority_group  " +
                "         inner join time_hired th on br.id_time_hired = th.id_time_hired  " +
                "where :currentTime between brs.registration_start_time and brs.registration_end_time  " +
                "  and pg.id_priority_group = :idPriorityGroup  " +
                "  and yg.id_year_group = :idYearGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        KtxUser ktxUser = (KtxUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        query.setParameter("currentTime", new Date().getTime());
        query.setParameter("idPriorityGroup", ktxUser.getIdPriorityGroup());
        query.setParameter("idYearGroup", ktxUser.getIdYearGroup());
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                TimeHired timeHired = new TimeHired();
                timeHired.setIdTimeHired(ValueUtil.getIntegerByObject(obj[0]));
                timeHired.setTimeStarted(ValueUtil.getLongByObject(obj[1]));
                timeHired.setTimeEnded(ValueUtil.getLongByObject(obj[2]));
                timeHired.setStatus(ValueUtil.getIntegerByObject(obj[3]));
                timeHired.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
                timeHired.setTimeModified(ValueUtil.getLongByObject(obj[5]));
                timeHired.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                timeHired.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                timeHired.setCodeTimeHired(ValueUtil.getStringByObject(obj[8]));
                timeHired.setTitleTimeHired(ValueUtil.getStringByObject(obj[9]));
                return Optional.of(timeHired);
            }
        }
        return Optional.empty();
    }

    private long countTimeHiredDto(FindAllTimeHiredRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(0) from time_hired where 1=1 ");
        if (StringUtils.isNotBlank(request.getKeyword())){
            sb.append("   and (time_hired.time_started REGEXP '[' + :keyword + ']') OR " +
                    "       (time_hired.time_ended REGEXP '[' + :keyword + ']') ");
        }
        Query query = entityManager.createNativeQuery(sb.toString());
        if (StringUtils.isNotBlank(request.getKeyword())) {
            query.setParameter("keyword", request.getKeyword());
        }
        return  ValueUtil.getLongByObject(query.getFirstResult());
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
        timeHired.setCodeTimeHired(ValueUtil.getStringByObject(obj[8]));
        timeHired.setTitleTimeHired(ValueUtil.getStringByObject(obj[9]));
        return timeHired;
    }
}
