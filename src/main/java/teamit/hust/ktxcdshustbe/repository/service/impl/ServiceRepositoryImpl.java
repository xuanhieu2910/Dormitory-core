package teamit.hust.ktxcdshustbe.repository.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.entity.Service;
import teamit.hust.ktxcdshustbe.repository.service.ServiceRepositoryCustom;
import teamit.hust.ktxcdshustbe.utility.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class ServiceRepositoryImpl implements ServiceRepositoryCustom {


    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Service> findAllService() {
        StringBuilder sb = new StringBuilder();
        sb.append("select se.id_service, se.title, se.description, " +
                "       se.status, se.code_service, se.time_created, " +
                "       se.time_modified, se.id_user_created, se.id_user_modified " +
                "from service se   " +
                "where 1 = 1 ");
        Query query = entityManager.createNativeQuery(sb.toString());
        List<Object[]> results = query.getResultList();
        List<Service> services = new ArrayList<>();
        if (!CollectionUtils.isEmpty(results)) {
            for(Object[] obj: results){
                services.add(writeDataService(obj));
            }
        }
        return services;
    }

    @Override
    public List<Service> findAllServiceByServiceIds(List<Integer> serviceIds) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select se.id_service, se.title, se.description, " +
                "       se.status, se.code_service, se.time_created, " +
                "       se.time_modified, se.id_user_created, id_user_modified " +
                "from service se   " +
                "where se.id_service in (:serviceIds)  ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("serviceIds", serviceIds);
        List<Object[]> result = query.getResultList();
        List<Service> services = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for(Object[] obj : result){
               services.add(writeDataService(obj));
            }
        }
        return services;
    }

    @Override
    public List<Service> findAllServiceByListServiceCode(List<String> serviceCode) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ser.id_service, ser.title, ser.description, " +
                "       ser.status, ser.code_service, ser.time_created, " +
                "       ser.time_modified, ser.id_user_created, " +
                "       ser.id_user_modified " +
                "from service ser " +
                "where ser.code_service in (:codesService) ");
        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("codesService", serviceCode);
        List<Service> services = new ArrayList<>();
        List<Object[]> result = query.getResultList();
        if (!CollectionUtils.isEmpty(result)){
            for (Object[] obj : result){
                services.add(writeDataService(obj));
            }
            return services;
        }
        return null;
    }

    private Service writeDataService(Object[] obj) {
        Service service = new Service();
        service.setIdService(ValueUtil.getIntegerByObject(obj[0]));
        service.setTitle(ValueUtil.getStringByObject(obj[1]));
        service.setDescription(ValueUtil.getStringByObject(obj[2]));
        service.setStatus(ValueUtil.getIntegerByObject(obj[3]));
        service.setCodeService(ValueUtil.getStringByObject(obj[4]));
        service.setTimeCreated(ValueUtil.getLongByObject(obj[5]));
        service.setTimeModified(ValueUtil.getLongByObject(obj[6]));
        service.setIdUserCreated(ValueUtil.getIntegerByObject(obj[7]));
        service.setIdUserModified(ValueUtil.getIntegerByObject(obj[8]));
        return service;
    }
}
