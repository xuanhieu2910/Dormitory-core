package teamit.hust.ktxcdshustbe.service.customService.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.service.ServiceRepository;
import teamit.hust.ktxcdshustbe.service.customService.CustomServicesService;

import java.util.List;

@Service
public class CustomServicesServiceImpl implements CustomServicesService {


    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public List<teamit.hust.ktxcdshustbe.entity.Service> findAllServiceByCodesService(List<String> codesService) {
        List<teamit.hust.ktxcdshustbe.entity.Service> services = serviceRepository.findAllServiceByListServiceCode(codesService);
        if (CollectionUtils.isEmpty(services) || services.size() != codesService.size()){
            throw new NotFoundException();
        }
        return services;
    }
}
