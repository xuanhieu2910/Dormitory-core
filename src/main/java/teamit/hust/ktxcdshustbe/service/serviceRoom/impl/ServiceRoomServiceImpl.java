package teamit.hust.ktxcdshustbe.service.serviceRoom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import teamit.hust.ktxcdshustbe.dto.serviceRoom.ServiceRoomDto;
import teamit.hust.ktxcdshustbe.entity.ServiceRoom;
import teamit.hust.ktxcdshustbe.exception.NotFoundException;
import teamit.hust.ktxcdshustbe.repository.service.ServiceRepository;
import teamit.hust.ktxcdshustbe.repository.serviceRoom.ServiceRoomRepository;
import teamit.hust.ktxcdshustbe.response.serviceRoom.ServiceRoomResponse;
import teamit.hust.ktxcdshustbe.service.serviceRoom.ServiceRoomService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceRoomServiceImpl implements ServiceRoomService {

    @Autowired
    ServiceRoomRepository serviceRoomRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Override
    public List<ServiceRoomDto> findAllServicesRoomByRoomId(Integer roomId) {
        return serviceRoomRepository.findAllServicesRoomByRoomId(roomId);
    }

    @Override
    public List<ServiceRoomResponse> findAllServicesRoom() {
        List<teamit.hust.ktxcdshustbe.entity.Service> listService = serviceRepository.findAllService();
        return convertToService(listService);
    }

    @Override
    public void storedServiceRooms(List<ServiceRoom> serviceRoom){
        serviceRoomRepository.saveAll(serviceRoom);
    }

    @Override
    public List<ServiceRoomDto> findServicesRoomByCodeRoomAndCodesService(String codeRoom, List<String> codesService) {
        List<ServiceRoomDto> serviceRooms = serviceRoomRepository.findServicesRoomByCodeRoomAndCodesService(codeRoom, codesService);
        if (CollectionUtils.isEmpty(serviceRooms) || serviceRooms.size() != codesService.size()){
            throw new NotFoundException();
        }
        return serviceRooms;
    }


    private List<ServiceRoomResponse> convertToService(List<teamit.hust.ktxcdshustbe.entity.Service> services){
        List<ServiceRoomResponse> responses = new ArrayList<>();
        for (teamit.hust.ktxcdshustbe.entity.Service service: services){
            responses.add(ServiceRoomResponse.builder()
                    .codeService(service.getCodeService())
                    .title(service.getTitle())
                    .description(service.getDescription())
                    .build());
        }
        return responses;
    }
}
