package teamit.hust.ktxcdshustbe.repository.priorityGroup.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.PriorityGroup;
import teamit.hust.ktxcdshustbe.repository.priorityGroup.PriorityGroupRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.List;
import java.util.Optional;

public class PriorityGroupRepositoryImpl implements PriorityGroupRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Optional<PriorityGroup> findPriorityGroupByTitle(String titlePriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select priority_group.id_priority_group, priority_group_code,  " +
                "       title, description, time_created, " +
                "       time_modified, id_user_created, id_user_modified " +
                "from priority_group where priority_group.title = :titlePriorityGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("titlePriorityGroup", titlePriorityGroup);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                return Optional.of(writeDataPriorityGroup(obj));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<PriorityGroup> findPriorityGroupByIdPriorityGroup(Integer idPriorityGroup) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select priority_group.id_priority_group, priority_group_code,  " +
                "       title, description, time_created, " +
                "       time_modified, id_user_created, id_user_modified " +
                "from priority_group where priority_group.id_priority_group = :idPriorityGroup ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("idPriorityGroup", idPriorityGroup);
        List<Object[]> results = query.getResultList();
        if (!CollectionUtils.isEmpty(results)) {
            for (Object[] obj: results){
                return Optional.of(writeDataPriorityGroup(obj));
            }
        }
        return Optional.empty();
    }

    private PriorityGroup writeDataPriorityGroup(Object[] obj) {
        PriorityGroup priorityGroup = new PriorityGroup();
        priorityGroup.setIdPriorityGroup(ValueUtil.getIntegerByObject(obj[0]));
        priorityGroup.setPriorityGroupCode(ValueUtil.getStringByObject(obj[1]));
        priorityGroup.setTitle(ValueUtil.getStringByObject(obj[2]));
        priorityGroup.setDescription(ValueUtil.getStringByObject(obj[3]));
        priorityGroup.setTimeCreated(ValueUtil.getLongByObject(obj[4]));
        priorityGroup.setTimeModified(ValueUtil.getLongByObject(obj[5]));
        priorityGroup.setIdUserCreated(ValueUtil.getIntegerByObject(obj[6]));
        priorityGroup.setIdUserModified(ValueUtil.getIntegerByObject(obj[7]));
        return priorityGroup;
    }
}
