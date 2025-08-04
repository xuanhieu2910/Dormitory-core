package teamit.hust.ktxcdshustbe.repository.batchesRegistration.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.BatchesRegistrationDetailDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.FindAllBatchesRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.batchesRegistration.FindAllDepartmentBatchesRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.batchesYearGroupRegistration.BatchesYearGroupRegistrationDto;
import teamit.hust.ktxcdshustbe.dto.timeHired.TimeHiredCurrentDto;
import teamit.hust.ktxcdshustbe.entity.BatchesRegistration;
import teamit.hust.ktxcdshustbe.repository.batchesRegistration.BatchesRegistrationRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.request.batchesRegistration.FindAllDepartmentInBatchesRegistrationRequest;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.*;

public class BatchesRegistrationRepositoryImpl implements BatchesRegistrationRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Page<FindAllBatchesRegistrationDto> findAllBatchesRegistration(FindAllBatchesRegistrationRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select bare.id_batches_registration, bare.title, bare.code_batches_registration,    " +
                "        bare.start_time, bare.end_time,    " +
                "        se.id_semester, se.title, se.code_semester,    " +
                "        bygr.id_batches_year_group_registration, yg.id_year_group,    " +
                "        yg.title, bygr.status, bygr.time_created, bygr.time_modified , " +
                "        th.id_time_hired, th.time_started, th.time_ended, " +
                "        th.code_time_hired, th.title_time_hired " +
                " from batches_registration bare    " +
                "     inner join semester se on bare.id_semester = se.id_semester    " +
                "     inner join batches_year_group_registration bygr on bare.id_batches_registration = bygr.id_batches_registration    " +
                "     inner join year_group yg on bygr.id_year_group = yg.id_year_group " +
                "     inner join time_hired th on bare.id_time_hired = th.id_time_hired " +
                " where 1 = 1  ");
        setConditionalFindAllBatchesRegistration(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllBatchesRegistration(query, request);
        PageUtils.buildQuery(pageable, query);
        Map<Integer, FindAllBatchesRegistrationDto> registrationDtoMap = new HashMap<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                Integer idBatchesRegistration = ValueUtil.getIntegerByObject(obj[0]);
                if (registrationDtoMap.containsKey(idBatchesRegistration)){
                    BatchesYearGroupRegistrationDto batchesYearGroupRegistrationDto = new BatchesYearGroupRegistrationDto();
                    batchesYearGroupRegistrationDto.setIdBatchesYearGroupRegistration(ValueUtil.getIntegerByObject(obj[8]));
                    batchesYearGroupRegistrationDto.setIdYearGroup(ValueUtil.getIntegerByObject(obj[9]));
                    batchesYearGroupRegistrationDto.setTitleYearGroup(ValueUtil.getStringByObject(obj[10]));
                    batchesYearGroupRegistrationDto.setStatus(ValueUtil.getIntegerByObject(obj[11]));
                    batchesYearGroupRegistrationDto.setTimeCreated(ValueUtil.getLongByObject(obj[12]));
                    batchesYearGroupRegistrationDto.setTimeModified(ValueUtil.getLongByObject(obj[13]));
                    registrationDtoMap.get(idBatchesRegistration).getBatchesYearGroupRegistrationDtos().add(batchesYearGroupRegistrationDto);
                } else {
                    FindAllBatchesRegistrationDto batchesRegistrationDto = new FindAllBatchesRegistrationDto();
                    batchesRegistrationDto.setIdBatchesRegistration(idBatchesRegistration);
                    batchesRegistrationDto.setTitleBatchesRegistration(ValueUtil.getStringByObject(obj[1]));
                    batchesRegistrationDto.setCodeBatchesRegistration(ValueUtil.getStringByObject(obj[2]));
                    batchesRegistrationDto.setStartTime(ValueUtil.getLongByObject(obj[3]));
                    batchesRegistrationDto.setEndTime(ValueUtil.getLongByObject(obj[4]));
                    batchesRegistrationDto.setIdSemester(ValueUtil.getIntegerByObject(obj[5]));
                    batchesRegistrationDto.setTitleSemester(ValueUtil.getStringByObject(obj[6]));
                    batchesRegistrationDto.setCodeSemester(ValueUtil.getStringByObject(obj[7]));

                    TimeHiredCurrentDto timeHiredCurrentDto = new TimeHiredCurrentDto();
                    timeHiredCurrentDto.setIdTimeHired(ValueUtil.getIntegerByObject(obj[14]));
                    timeHiredCurrentDto.setTimeStarted(ValueUtil.getLongByObject(obj[15]));
                    timeHiredCurrentDto.setTimeEnded(ValueUtil.getLongByObject(obj[16]));
                    timeHiredCurrentDto.setCodeTimeHired(ValueUtil.getStringByObject(obj[17]));
                    timeHiredCurrentDto.setTitleTimeHired(ValueUtil.getStringByObject(obj[18]));

                    batchesRegistrationDto.setTimeHiredCurrentDto(timeHiredCurrentDto);
                    List<BatchesYearGroupRegistrationDto> batchesYearGroupRegistrationDtos = new ArrayList<>();
                    BatchesYearGroupRegistrationDto batchesYearGroupRegistrationDto = new BatchesYearGroupRegistrationDto();
                    batchesYearGroupRegistrationDto.setIdBatchesYearGroupRegistration(ValueUtil.getIntegerByObject(obj[8]));
                    batchesYearGroupRegistrationDto.setIdYearGroup(ValueUtil.getIntegerByObject(obj[9]));
                    batchesYearGroupRegistrationDto.setTitleYearGroup(ValueUtil.getStringByObject(obj[10]));
                    batchesYearGroupRegistrationDto.setStatus(ValueUtil.getIntegerByObject(obj[11]));
                    batchesYearGroupRegistrationDto.setTimeCreated(ValueUtil.getLongByObject(obj[12]));
                    batchesYearGroupRegistrationDto.setTimeModified(ValueUtil.getLongByObject(obj[13]));
                    batchesYearGroupRegistrationDtos.add(batchesYearGroupRegistrationDto);
                    batchesRegistrationDto.setBatchesYearGroupRegistrationDtos(batchesYearGroupRegistrationDtos);
                    registrationDtoMap.put(idBatchesRegistration, batchesRegistrationDto);
                }
            }
        }
        return new PageImpl<>(registrationDtoMap.values().stream().toList(), pageable, countFindAllBatchesRegistration(request));
    }

    @Override
    public Optional<BatchesRegistrationDetailDto> getDetailBatchesRegistration(String codeBatchesRegistration) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select bare.id_batches_registration, bare.title, bare.code_batches_registration, " +
                        "       bare.start_time, bare.end_time, bare.notes, bare.description,  " +
                        "       se.id_semester, se.title, se.code_semester,  " +
                        "       bygr.id_batches_year_group_registration, yg.id_year_group,  " +
                        "       yg.title, bygr.status, bygr.time_created, bygr.time_modified, " +
                        "       th.id_time_hired, th.code_time_hired, th.title_time_hired, " +
                        "       th.time_started, th.time_ended " +
                        "from batches_registration bare  " +
                        "    inner join semester se on bare.id_semester = se.id_semester    " +
                        "    inner join batches_year_group_registration bygr on bare.id_batches_registration = bygr.id_batches_registration  " +
                        "    inner join year_group yg on bygr.id_year_group = yg.id_year_group " +
                        "    inner join time_hired th on bare.id_time_hired = th.id_time_hired " +
                        "where bare.code_batches_registration = :codeBatchesRegistration  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeBatchesRegistration", codeBatchesRegistration);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            BatchesRegistrationDetailDto detailDto = new BatchesRegistrationDetailDto();
            detailDto.setIdBatchesRegistration(ValueUtil.getIntegerByObject(result.get(0)[0]));
            detailDto.setTitleBatchesRegistration(ValueUtil.getStringByObject(result.get(0)[1]));
            detailDto.setCodeBatchesRegistration(ValueUtil.getStringByObject(result.get(0)[2]));
            detailDto.setStartTime(ValueUtil.getLongByObject(result.get(0)[3]));
            detailDto.setEndTime(ValueUtil.getLongByObject(result.get(0)[4]));
            detailDto.setNotes(ValueUtil.getStringByObject(result.get(0)[5]));
            detailDto.setDescription(ValueUtil.getStringByObject(result.get(0)[6]));
            detailDto.setIdSemester(ValueUtil.getIntegerByObject(result.get(0)[7]));
            detailDto.setTitleSemester(ValueUtil.getStringByObject(result.get(0)[8]));
            detailDto.setCodeSemester(ValueUtil.getStringByObject(result.get(0)[9]));
            TimeHiredCurrentDto timeHiredCurrentDto = new TimeHiredCurrentDto();
            timeHiredCurrentDto.setIdTimeHired(ValueUtil.getIntegerByObject(result.get(0)[16]));
            timeHiredCurrentDto.setCodeTimeHired(ValueUtil.getStringByObject(result.get(0)[17]));
            timeHiredCurrentDto.setTitleTimeHired(ValueUtil.getStringByObject(result.get(0)[18]));
            timeHiredCurrentDto.setTimeStarted(ValueUtil.getLongByObject(result.get(0)[19]));
            timeHiredCurrentDto.setTimeEnded(ValueUtil.getLongByObject(result.get(0)[20]));
            detailDto.setTimeHiredCurrentDto(timeHiredCurrentDto);
            List<BatchesYearGroupRegistrationDto> batchesYearGroupRegistrationDtos = new ArrayList<>();

            for (Object[] obj : result){
                BatchesYearGroupRegistrationDto yearGroupRegistrationDto = new BatchesYearGroupRegistrationDto();
                yearGroupRegistrationDto.setIdBatchesYearGroupRegistration(ValueUtil.getIntegerByObject(obj[10]));
                yearGroupRegistrationDto.setIdYearGroup(ValueUtil.getIntegerByObject(obj[11]));
                yearGroupRegistrationDto.setTitleYearGroup(ValueUtil.getStringByObject(obj[12]));
                yearGroupRegistrationDto.setStatus(ValueUtil.getIntegerByObject(obj[13]));
                yearGroupRegistrationDto.setTimeCreated(ValueUtil.getLongByObject(obj[14]));
                yearGroupRegistrationDto.setTimeModified(ValueUtil.getLongByObject(obj[15]));
                batchesYearGroupRegistrationDtos.add(yearGroupRegistrationDto);
            }
            detailDto.setBatchesYearGroupRegistrationDtos(batchesYearGroupRegistrationDtos);
            return Optional.of(detailDto);
        }
        return Optional.empty();
    }

    @Override
    public boolean checkNotExitsBatchesRegistration(List<String> codeYearGroups, Long startDate, Long endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("select case  " +
                "           when exists(select 1  " +
                "                       from batches_registration br  " +
                "                                inner join semester se on br.id_semester = se.id_semester  " +
                "                                inner join batches_year_group_registration bygr  " +
                "                                           on br.id_batches_registration = bygr.id_batches_registration  " +
                "                                inner join year_group yg on bygr.id_year_group = yg.id_year_group  " +
                "                       where yg.code_year_group in (:codeYearGroups)  " +
                "                         and (((:startDate >= br.start_time and :startDate <= br.end_time) or  " +
                "                               (:endDate >= br.start_time and :endDate <= br.end_time))  " +
                "                           or (:startDate <= br.start_time and :endDate >= br.end_time))) then 1  " +
                "           else 0 end result ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeYearGroups", codeYearGroups);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return ValueUtil.getIntegerByObject(query.getSingleResult()).equals(0);
    }

    @Override
    public Optional<BatchesRegistration> findBatchesRegistrationByCodeBatchesRegistration(String codeBatchesRegistration) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select br.id_batches_registration, br.title, br.code_batches_registration,  " +
                "       br.id_time_hired, br.id_semester, br.description, br.notes,  " +
                "       br.time_created, br.time_modified, br.id_user_created,  " +
                "       br.id_user_modified, br.start_time, br.end_time  " +
                "from batches_registration br  " +
                "    inner join time_hired th on br.id_time_hired = th.id_time_hired  " +
                "    inner join semester se on br.id_semester = se.id_semester  " +
                "where br.code_batches_registration = :codeBatchesRegistration ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codeBatchesRegistration", codeBatchesRegistration);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataBatchesRegistration(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<BatchesRegistration> getBatchesRegistrationCurrentByIdYearGroupAndIdPriorityGroup(Long timeCurrent,
                                                                                                      Integer idPriorityGroup,
                                                                                                      Integer idYearGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                select br.id_batches_registration, br.title, br.code_batches_registration,
                       br.id_time_hired, br.id_semester, br.description, br.notes,
                       br.time_created, br.time_modified, br.id_user_created,
                       br.id_user_modified, br.start_time, br.end_time
                from batches_registration br
                    inner join batches_year_group_registration bygr
                        on br.id_batches_registration = bygr.id_batches_registration
                    inner join year_group yg on bygr.id_year_group = yg.id_year_group
                    inner join batches_registration_schedule brs on br.id_batches_registration = brs.id_batches_registration
                    inner join priority_group pg on brs.id_priority_group = pg.id_priority_group
                where :currentTime between br.start_time and br.end_time
                and pg.id_priority_group = :idPriorityGroup
                and yg.id_year_group = :idYearGroup
                """);
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("currentTime", timeCurrent);
        query.setParameter("idPriorityGroup", idPriorityGroup);
        query.setParameter("idYearGroup", idYearGroup);
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                return Optional.of(writeDataBatchesRegistration(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Page<FindAllDepartmentBatchesRegistrationDto>
    findAllDepartmentBatchesRegistration(FindAllDepartmentInBatchesRegistrationRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select de.id_department, de.code_department, " +
                "       de.title, br.code_batches_registration " +
                "from batches_registration br " +
                "    inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration " +
                "    inner join room ro on brr.id_room = ro.id_room " +
                "    inner join department de on ro.id_department = de.id_department " +
                "where br.code_batches_registration = :codeBatchesRegistration " +
                "group by de.id_department, de.code_department, de.title, br.code_batches_registration ");
        setConditionalFindAllDepartmentBatchesRegistration(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllDepartmentBatchesRegistration(query, request);
        PageUtils.buildQuery(pageable, query);
        List<Object[]> result = query.getResultList();
        List<FindAllDepartmentBatchesRegistrationDto> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                dtos.add(writeDataFindAllDepartmentBatchesRegistration(obj));
            }
        }
        return new PageImpl<>(dtos, pageable, countFindAllDepartmentBatchesRegistration(request));
    }

    private long countFindAllDepartmentBatchesRegistration(FindAllDepartmentInBatchesRegistrationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(de.id_department) " +
                "from batches_registration br " +
                "    inner join batches_registration_room brr on br.id_batches_registration = brr.id_batches_registration " +
                "    inner join room ro on brr.id_room = ro.id_room " +
                "    inner join department de on ro.id_department = de.id_department " +
                "where br.code_batches_registration = :codeBatchesRegistration " +
                "group by de.id_department, de.code_department, de.title, br.code_batches_registration ");
        setConditionalFindAllDepartmentBatchesRegistration(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllDepartmentBatchesRegistration(query, request);
        return ValueUtil.getLongByObject(query.getFirstResult());
    }

    private FindAllDepartmentBatchesRegistrationDto writeDataFindAllDepartmentBatchesRegistration(Object[] obj) {
        FindAllDepartmentBatchesRegistrationDto dto = new FindAllDepartmentBatchesRegistrationDto();
        dto.setIdDepartment(ValueUtil.getIntegerByObject(obj[0]));
        dto.setCodeDepartment(ValueUtil.getStringByObject(obj[1]));
        dto.setTitleDepartment(ValueUtil.getStringByObject(obj[2]));
        dto.setCodeBatchesRegistration(ValueUtil.getStringByObject(obj[3]));
        return dto;
    }

    private void setParameterFindAllDepartmentBatchesRegistration(Query query, FindAllDepartmentInBatchesRegistrationRequest request) {
        query.setParameter("codeBatchesRegistration", request.getCodeBatchesRegistration());
    }

    private void setConditionalFindAllDepartmentBatchesRegistration(StringBuilder sb,
                                                                    FindAllDepartmentInBatchesRegistrationRequest request) {
        sb.append(" order by de.id_department desc  ");
    }

    private BatchesRegistration writeDataBatchesRegistration(Object[] obj) {
        BatchesRegistration batchesRegistration = new BatchesRegistration();
        batchesRegistration.setIdBatchesRegistration(ValueUtil.getIntegerByObject(obj[0]));
        batchesRegistration.setTitle(ValueUtil.getStringByObject(obj[1]));
        batchesRegistration.setCodeBatchesRegistration(ValueUtil.getStringByObject(obj[2]));
        batchesRegistration.setIdTimeHired(ValueUtil.getIntegerByObject(obj[3]));
        batchesRegistration.setIdSemester(ValueUtil.getIntegerByObject(obj[4]));
        batchesRegistration.setDescription(ValueUtil.getStringByObject(obj[5]));
        batchesRegistration.setNotes(ValueUtil.getStringByObject(obj[6]));
        batchesRegistration.setTimeCreated(ValueUtil.getLongByObject(obj[7]));
        batchesRegistration.setTimeModified(ValueUtil.getLongByObject(obj[8]));
        batchesRegistration.setIdUserCreated(ValueUtil.getIntegerByObject(obj[9]));
        batchesRegistration.setIdUserModified(ValueUtil.getIntegerByObject(obj[10]));
        batchesRegistration.setStartTime(ValueUtil.getLongByObject(obj[11]));
        batchesRegistration.setEndTime(ValueUtil.getLongByObject(obj[12]));
        return batchesRegistration;
    }

    private long countFindAllBatchesRegistration(FindAllBatchesRegistrationRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0)   " +
                "from batches_registration bare   " +
                "    inner join semester se on bare.id_semester = se.id_semester   " +
                "    inner join batches_year_group_registration bygr on bare.id_batches_registration = bygr.id_batches_registration   " +
                "    inner join year_group yg on bygr.id_year_group = yg.id_year_group   " +
                "where 1 = 1    ");
        setConditionalFindAllBatchesRegistration(sb, request);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllBatchesRegistration(query, request);
        return ValueUtil.getIntegerByObject(query.getFirstResult());
    }

    private void setParameterFindAllBatchesRegistration(Query query, FindAllBatchesRegistrationRequest request) {
        if (StringUtils.isNotBlank(request.getTitleBatchesRegistration())){
            query.setParameter("titleBatchesRegistration", request.getTitleBatchesRegistration());
        }
    }

    private void setConditionalFindAllBatchesRegistration(StringBuilder sb, FindAllBatchesRegistrationRequest request) {
        if (StringUtils.isNotBlank(request.getTitleBatchesRegistration())){
            sb.append(" and bare.title REGEXP :titleBatchesRegistration ");
        }
        sb.append(" order by bare.id_batches_registration desc  ");
    }
}
