package teamit.hust.ktxcdshustbe.repository.userInstance.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.KtxUserInstance;
import teamit.hust.ktxcdshustbe.repository.userInstance.KtxUserInstanceRepositoryCustom;
import teamit.hust.ktxcdshustbe.request.userInstance.FindAllUserInstanceRequest;
import teamit.hust.ktxcdshustbe.utility.PageUtils;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class KtxUserInstanceRepositoryImpl implements KtxUserInstanceRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public Page<KtxUserInstance> findAllUserInstance(FindAllUserInstanceRequest request, Pageable pageable) {
        StringBuilder sb = new StringBuilder();
        sb.append("select kti.id_ktx_user_instance, id_user_created,  " +
                "       id_user_modified, time_created, time_modified,  " +
                "       value, error from ktx_user_instance kti  " +
                "where 1=1  ");
        setConditionFindAllUserInstance(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllUserInstance(request, query);
        PageUtils.buildQuery(pageable, query);
        List<KtxUserInstance> instanceDtos = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                instanceDtos.add(writeDataFindAllUserInstance(obj));
            }
        }
        return new PageImpl<>(instanceDtos, pageable, countFindAllUserInstance(request));
    }

    @Override
    public List<KtxUserInstance> findAllUserInstanceByIds(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("select kti.id_ktx_user_instance, id_user_created,  " +
                "       id_user_modified, time_created, time_modified,  " +
                "       value, error from ktx_user_instance kti  " +
                "where  kti.id_ktx_user_instance in (:ids) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("ids", ids);
        List<Object[]> result = query.getResultList();
        List<KtxUserInstance> instances = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result) {
                instances.add(writeDataFindAllUserInstance(obj));
            }
        }
        return instances;
    }

    private long countFindAllUserInstance(FindAllUserInstanceRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("select count(0) " +
                "from ktx_user_instance kti " +
                "where 1 = 1 ");
        setConditionFindAllUserInstance(request, sb);
        Query query = entityManager.createNativeQuery(sb.toString());
        setParameterFindAllUserInstance(request, query);
        return ValueUtil.getIntegerByObject(query.getSingleResult());
    }

    private KtxUserInstance writeDataFindAllUserInstance(Object[] obj) {
        KtxUserInstance instance = new KtxUserInstance();
        instance.setIdKtxUserInstance(ValueUtil.getIntegerByObject(obj[0]));
        instance.setIdUserCreated(ValueUtil.getIntegerByObject(obj[1]));
        instance.setIdUserModified(ValueUtil.getIntegerByObject(obj[2]));
        instance.setTimeCreated(ValueUtil.getStringByObject(obj[3]));
        instance.setTimeModified(ValueUtil.getStringByObject(obj[4]));
        instance.setValue(ValueUtil.getStringByObject(obj[5]));
        instance.setError(ValueUtil.getIntegerByObject(obj[6]));
        return instance;
    }

    private void setParameterFindAllUserInstance(FindAllUserInstanceRequest request, Query query) {
        if (ObjectUtils.isNotEmpty(request.getIsError())) {
            query.setParameter("error", request.getIsError());
        }
    }

    private void setConditionFindAllUserInstance(FindAllUserInstanceRequest request, StringBuilder sb) {
        if (ObjectUtils.isNotEmpty(request.getIsError())) {
            sb.append(" and kti.error = :error ");
        }
        sb.append(" order by kti.id_ktx_user_instance ");
    }
}
