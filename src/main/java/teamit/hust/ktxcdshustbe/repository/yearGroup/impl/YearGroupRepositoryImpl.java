package teamit.hust.ktxcdshustbe.repository.yearGroup.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.yearGroup.FindAllYearGroupsDto;
import teamit.hust.ktxcdshustbe.entity.YearGroup;
import teamit.hust.ktxcdshustbe.repository.yearGroup.YearGroupRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.yearGroup.FindAllYearGroupsRequest;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class YearGroupRepositoryImpl implements YearGroupRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<YearGroup> findYearGroupByTitle(String titleYearGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select year_group.id_year_group, code_year_group, " +
                "       title, description, time_created, time_modified,  " +
                "       id_user_created, id_user_modified  " +
                "from year_group where year_group.title = :titleYearGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titleYearGroup", titleYearGroup);
        List<Object[]> results = query.getResultList();
        if(!CollectionUtils.isEmpty(results)){
            for (Object[] result : results) {
                return Optional.of(writeObjYearGroup(result));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<YearGroup> findYearGroupByIdYearGroup(Integer idYearGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select year_group.id_year_group, code_year_group, " +
                "       title, description, time_created, time_modified,  " +
                "       id_user_created, id_user_modified  " +
                "from year_group where year_group.id_year_group = :idYearGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idYearGroup", idYearGroup);
        List<Object[]> results = query.getResultList();
        if(!CollectionUtils.isEmpty(results)){
            for (Object[] result : results) {
                return Optional.of(writeObjYearGroup(result));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<YearGroup>> findYearGroupsByListCodes(List<String> codes) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_year_group, code_year_group, title, " +
                "       description, time_created, time_modified, " +
                "       id_user_created, id_user_modified " +
                " from year_group  " +
                " where year_group.code_year_group in (:codes)  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codes", codes);
        List<Object[]> results = query.getResultList();
        if(!CollectionUtils.isEmpty(results)){
            List<YearGroup> yearGroups = new ArrayList<>();
            for (Object[] result : results) {
                yearGroups.add(writeObjYearGroup(result));
            }
            return Optional.of(yearGroups);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<YearGroup>> findYearGroupsByIds(List<Integer> idsYearGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select id_year_group, code_year_group, title, " +
                "       description, time_created, time_modified, " +
                "       id_user_created, id_user_modified " +
                " from year_group  " +
                " where year_group.id_year_group in (:ids)  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("ids", idsYearGroup);
        List<Object[]> results = query.getResultList();
        if(!CollectionUtils.isEmpty(results)){
            List<YearGroup> yearGroups = new ArrayList<>();
            for (Object[] result : results) {
                yearGroups.add(writeObjYearGroup(result));
            }
            return Optional.of(yearGroups);
        }
        return Optional.empty();
    }

    private YearGroup writeObjYearGroup(Object[] result) {
        YearGroup yearGroup = new YearGroup();
        yearGroup.setIdYearGroup(ValueUtil.getIntegerByObject(result[0]));
        yearGroup.setCodeYearGroup(ValueUtil.getStringByObject(result[1]));
        yearGroup.setTitle(ValueUtil.getStringByObject(result[2]));
        yearGroup.setDescription(ValueUtil.getStringByObject(result[3]));
        yearGroup.setTimeCreated(ValueUtil.getLongByObject(result[4]));
        yearGroup.setTimeModified(ValueUtil.getLongByObject(result[5]));
        yearGroup.setIdUserCreated(ValueUtil.getIntegerByObject(result[6]));
        yearGroup.setIdUserModified(ValueUtil.getIntegerByObject(result[7]));
        return yearGroup;
    }

    @Override
    public Page<FindAllYearGroupsDto> findAllYearGroups(FindAllYearGroupsRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT yg.id_year_group, yg.code_year_group, yg.title, yg.description, ")
                .append("        yg.time_created, yg.time_modified, yg.id_user_created, yg.id_user_modified, ")
                .append("        kuCreated.user_name, kuCreated.value, ")
                .append("        kuModified.user_name, kuModified.value ")
                .append(" FROM year_group yg ")
                .append("      INNER JOIN ktx_user kuCreated ON yg.id_user_created = kuCreated.id_ktx_user ")
                .append("      INNER JOIN ktx_user kuModified ON yg.id_user_modified = kuModified.id_ktx_user ")
                .append(" WHERE 1=1 ");

        setConditionFindAllYearGroups(request, sb);
        setOrderBy(request, sb);

        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllYearGroups(request, query);
        PageUtils.buildQuery(pageable, query);

        List<Object[]> result = query.getResultList();
        List<FindAllYearGroupsDto> dtos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                FindAllYearGroupsDto dto = new FindAllYearGroupsDto();
                dto.setIdYearGroup(ValueUtil.getIntegerByObject(obj[0]));
                dto.setCodeYearGroup(ValueUtil.getStringByObject(obj[1]));
                dto.setTitle(ValueUtil.getStringByObject(obj[2]));
                dto.setDescription(ValueUtil.getStringByObject(obj[3]));
                dto.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
                dto.setTimeModified(ValueUtil.getLongByObject(obj[5]));
                dto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
                dto.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
                dto.setUserNameCreated(ValueUtil.getStringByObject(obj[8]));
                dto.setValueCreated(ValueUtil.getStringByObject(obj[9]));
                dto.setUserNameModified(ValueUtil.getStringByObject(obj[10]));
                dto.setValueModified(ValueUtil.getStringByObject(obj[11]));
                dtos.add(dto);
            }
        }
        return new PageImpl<>(dtos, pageable, countFindAllYearGroups(request));
    }

    private void setConditionFindAllYearGroups(FindAllYearGroupsRequest request, StringBuilder sb) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            sb.append(" AND (yg.title LIKE :keyword OR yg.description LIKE :keyword) ");
        }
        if (StringUtils.isNotBlank(request.getCodeYearGroup())) {
            sb.append(" AND yg.code_year_group = :codeYearGroup ");
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            sb.append(" AND yg.title LIKE :title ");
        }
        if (StringUtils.isNotBlank(request.getDescription())) {
            sb.append(" AND yg.description LIKE :description ");
        }
        if (request.getIdUserCreated() != null) {
            sb.append(" AND yg.id_user_created = :idUserCreated ");
        }
        if (StringUtils.isNotBlank(request.getUserNameCreated())) {
            sb.append(" AND kuCreated.user_name LIKE :userNameCreated ");
        }
    }

    private void setParameterFindAllYearGroups(FindAllYearGroupsRequest request, Query query) {
        if (StringUtils.isNotBlank(request.getKeyword())) {
            query.setParameter("keyword", "%" + request.getKeyword() + "%");
        }

        if (StringUtils.isNotBlank(request.getCodeYearGroup())) {
            query.setParameter("codeYearGroup", request.getCodeYearGroup());
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            query.setParameter("title", "%" + request.getTitle() + "%");
        }
        if (StringUtils.isNotBlank(request.getDescription())) {
            query.setParameter("description", "%" + request.getDescription() + "%");
        }
        if (request.getIdUserCreated() != null) {
            query.setParameter("idUserCreated", request.getIdUserCreated());
        }
    }

    private void setOrderBy(FindAllYearGroupsRequest request, StringBuilder sb) {
        String sortBy = request.getSortBy();
        Set<String> allowedSortBy = Set.of("title", "description", "time_created", "time_modified");

        if (StringUtils.isNotBlank(sortBy) && allowedSortBy.contains(sortBy)) {
            sb.append(" ORDER BY yg.").append(sortBy);
            if (StringUtils.equalsIgnoreCase(request.getSortOrder(), "DESC")) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        } else {
            sb.append(" ORDER BY yg.time_created DESC ");
        }
    }

    private long countFindAllYearGroups(FindAllYearGroupsRequest request) {
        StringBuilder sb = new StringBuilder("SELECT COUNT(yg.id_year_group) FROM year_group yg ")
                .append("      INNER JOIN ktx_user kuCreated ON yg.id_user_created = kuCreated.id_ktx_user ")
                .append("      INNER JOIN ktx_user kuModified ON yg.id_user_modified = kuModified.id_ktx_user ")
                .append(" WHERE 1=1 ");

        setConditionFindAllYearGroups(request, sb);

        Query query = entityManager.createNativeQuery(sb.toString());

        setParameterFindAllYearGroups(request, query);

        return ((Number) query.getSingleResult()).longValue();
    }

    @Override
    public Optional<FindAllYearGroupsDto> findYearGroupDetailsByCode(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT yg.id_year_group, yg.code_year_group, yg.title, yg.description, ")
                .append("        yg.time_created, yg.time_modified, yg.id_user_created, yg.id_user_modified, ")
                .append("        kuCreated.user_name, kuCreated.value, ")
                .append("        kuModified.user_name, kuModified.value ")
                .append(" FROM year_group yg ")
                .append("      INNER JOIN ktx_user kuCreated ON yg.id_user_created = kuCreated.id_ktx_user ")
                .append("      INNER JOIN ktx_user kuModified ON yg.id_user_modified = kuModified.id_ktx_user ")
                .append(" WHERE yg.code_year_group = :code ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("code", code);

        List<Object[]> result = query.getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }

        Object[] obj = result.get(0);
        FindAllYearGroupsDto dto = new FindAllYearGroupsDto();
        dto.setIdYearGroup(ValueUtil.getIntegerByObject(obj[0]));
        dto.setCodeYearGroup(ValueUtil.getStringByObject(obj[1]));
        dto.setTitle(ValueUtil.getStringByObject(obj[2]));
        dto.setDescription(ValueUtil.getStringByObject(obj[3]));
        dto.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        dto.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        dto.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        dto.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        dto.setUserNameCreated(ValueUtil.getStringByObject(obj[8]));
        dto.setValueCreated(ValueUtil.getStringByObject(obj[9]));
        dto.setUserNameModified(ValueUtil.getStringByObject(obj[10]));
        dto.setValueModified(ValueUtil.getStringByObject(obj[11]));

        return Optional.of(dto);
    }

    @Override
    public Optional<YearGroup> findByTitle(String title) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT id_year_group, code_year_group, title, description, ")
                .append("        time_created, time_modified, id_user_created, id_user_modified ")
                .append(" FROM year_group ")
                .append(" WHERE title = :title ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("title", title);

        List<Object[]> result = query.getResultList();

        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }

        Object[] obj = result.get(0);

        YearGroup yearGroup = new YearGroup();
        yearGroup.setIdYearGroup(ValueUtil.getIntegerByObject(obj[0]));
        yearGroup.setCodeYearGroup(ValueUtil.getStringByObject(obj[1]));
        yearGroup.setTitle(ValueUtil.getStringByObject(obj[2]));
        yearGroup.setDescription(ValueUtil.getStringByObject(obj[3]));
        yearGroup.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        yearGroup.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        yearGroup.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        yearGroup.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));

        return Optional.of(yearGroup);
    }

    @Override
    public Optional<YearGroup> findByCodeYearGroup(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT id_year_group, code_year_group, title, description, ")
                .append("        time_created, time_modified, id_user_created, id_user_modified ")
                .append(" FROM year_group ")
                .append(" WHERE code_year_group = :code ");

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("code", code);

        List<Object[]> result = query.getResultList();

        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }

        Object[] obj = result.get(0);
        YearGroup yearGroup = new YearGroup();
        yearGroup.setIdYearGroup(ValueUtil.getIntegerByObject(obj[0]));
        yearGroup.setCodeYearGroup(ValueUtil.getStringByObject(obj[1]));
        yearGroup.setTitle(ValueUtil.getStringByObject(obj[2]));
        yearGroup.setDescription(ValueUtil.getStringByObject(obj[3]));
        yearGroup.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        yearGroup.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        yearGroup.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        yearGroup.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));

        return Optional.of(yearGroup);
    }
}
