package teamit.hust.ktxcdshustbe.repository.semester.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.semester.FindAllSemesterDto;
import teamit.hust.ktxcdshustbe.entity.Semester;
import teamit.hust.ktxcdshustbe.repository.semester.SemesterRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.semester.FindAllSemesterRequest;
import teamit.hust.ktxcdshustbe.response.semester.FindAllSemesterResponse;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SemesterRepositoryImpl implements SemesterRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Page<FindAllSemesterDto> findAllSemester(FindAllSemesterRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select se.id_semester, se.title, se.time_created,  " +
                "       se.time_modified, se.status, se.id_user_created,  " +
                "       se.id_user_modified, se.code_semester,  " +
                "       ku_created.user_name, ku_created.full_name,  " +
                "       ku_modified.user_name, ku_modified.full_name  " +
                "from semester se  " +
                "        inner join ktx_user ku_created on se.id_user_created = ku_created.id_ktx_user  " +
                "        inner join ktx_user ku_modified on se.id_user_modified = ku_modified.id_ktx_user  " +
                "where 1 = 1 ");
        setConditionFindAllSemesterDto(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllSemesterDto(request, query);
        PageUtils.buildQuery(pageable, query);
        List<FindAllSemesterDto> semesterDtos = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                FindAllSemesterDto findAllSemesterDto = new FindAllSemesterDto();
                findAllSemesterDto.setIdSemester(ValueUtil.getIntegerByObject(obj[0]));
                findAllSemesterDto.setTitleSemester(ValueUtil.getStringByObject(obj[1]));
                findAllSemesterDto.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
                findAllSemesterDto.setStatus(ValueUtil.getIntegerByObject(obj[3]));
                findAllSemesterDto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[4]));
                findAllSemesterDto.setIdUserModified(ValueUtil.getIntegerByObject(obj[5]));
                findAllSemesterDto.setCodeSemester(ValueUtil.getStringByObject(obj[6]));
                findAllSemesterDto.setUserNameCreated(ValueUtil.getStringByObject(obj[7]));
                findAllSemesterDto.setFullNameCreated(ValueUtil.getStringByObject(obj[8]));
                findAllSemesterDto.setUserNameModified(ValueUtil.getStringByObject(obj[9]));
                findAllSemesterDto.setFullNameModified(ValueUtil.getStringByObject(obj[10]));
                semesterDtos.add(findAllSemesterDto);
            }
        }
        return new PageImpl<>(semesterDtos, pageable, countFindAllSemester(request));
    }

    private Long countFindAllSemester(FindAllSemesterRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(0) " +
                "from semester se  " +
                "        inner join ktx_user ku_created on se.id_user_created = ku_created.id_ktx_user  " +
                "        inner join ktx_user ku_modified on se.id_user_modified = ku_modified.id_ktx_user  " +
                "where 1 = 1 ");
        setConditionFindAllSemesterDto(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllSemesterDto(request, query);
        return ValueUtil.getLongByObject(query.getSingleResult());
    }

    private void setParameterFindAllSemesterDto(FindAllSemesterRequest request, Query query) {
        if (StringUtils.isNotBlank(request.getTitleSemester())){
            query.setParameter("titleSemester", request.getTitleSemester());
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            query.setParameter("status", request.getStatus());
        }
    }

    private void setConditionFindAllSemesterDto(FindAllSemesterRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getTitleSemester())){
            sb.append(" and se.title REGEXP :titleSemester ");
        }
        if (ObjectUtils.isNotEmpty(request.getStatus())){
            sb.append(" and se.status = :status ");
        }
        sb.append(" order by se.id_semester desc  ");
    }

    private Semester writeDataSemester(Object[] obj) {
        Semester semester = new Semester();
        semester.setIdSemester(ValueUtil.getIntegerByObject(obj[0]));
        semester.setTitle(ValueUtil.getStringByObject(obj[1]));
        semester.setTimeCreated(ValueUtil.getLongByObject(obj[2]));
        semester.setTimeModified(ValueUtil.getLongByObject(obj[3]));
        semester.setStatus(ValueUtil.getIntegerByObject(obj[4]));
        semester.setIdUserCreated(ValueUtil.getIntegerByObject(obj[5]));
        semester.setIdUserModified(ValueUtil.getIntegerByObject(obj[6]));
        return semester;
    }

    public Optional<FindAllSemesterResponse> fi
}
