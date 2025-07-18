package teamit.hust.ktxcdshustbe.repository.service;

import teamit.hust.ktxcdshustbe.entity.Service;

import java.util.List;

public interface ServiceRepositoryCustom {

    List<Service> findAllService();

    List<Service> findAllServiceByServiceIds(List<Integer> serviceIds);
    List<Service> findAllServiceByListServiceCode(List<String> serviceCode);
}
