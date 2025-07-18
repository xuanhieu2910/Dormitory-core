package teamit.hust.ktxcdshustbe.service.customService;

import teamit.hust.ktxcdshustbe.entity.Service;

import java.util.List;

public interface CustomServicesService {

    List<Service> findAllServiceByCodesService(List<String> codesService);

}
