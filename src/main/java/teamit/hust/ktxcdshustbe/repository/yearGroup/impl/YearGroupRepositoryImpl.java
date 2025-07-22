package teamit.hust.ktxcdshustbe.repository.yearGroup.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.collections4.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.YearGroup;
import teamit.hust.ktxcdshustbe.repository.yearGroup.YearGroupRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.List;
import java.util.Optional;

public class YearGroupRepositoryImpl implements YearGroupRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
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
}
